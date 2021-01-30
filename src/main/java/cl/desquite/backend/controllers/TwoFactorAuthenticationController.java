package cl.desquite.backend.controllers;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.apachecommons.CommonsLog;

import cl.desquite.backend.entities.Usuario;
import cl.desquite.backend.models.Credential2FA;
import cl.desquite.backend.services.IUsuarioService;
import cl.desquite.backend.utils.ResultadoProc;
import io.swagger.annotations.ApiOperation;

@RestController
@RequiredArgsConstructor
@RequestMapping("/two-factor")
@CommonsLog
public class TwoFactorAuthenticationController {

    @Autowired
    IUsuarioService usuarioService;

    @Autowired
    UserDetailsService userDetailsService;

    private final GoogleAuthenticator gAuth;

    // private AuthenticationManager authenticationManager;
    // private final CredentialRepository credentialRepository;

    @ApiOperation(value = "Genera una imagen PNG del código QR para la 2FA")
    @SneakyThrows
    @GetMapping("/v1/generate")
    public void generate(Authentication auth, HttpServletResponse response) {
        final GoogleAuthenticatorKey key = gAuth.createCredentials(auth.getName());

        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        String otpAuthURL = GoogleAuthenticatorQRGenerator.getOtpAuthTotpURL("El Desquite", auth.getName(), key);
        BitMatrix bitMatrix = qrCodeWriter.encode(otpAuthURL, BarcodeFormat.QR_CODE, 250, 250);

        ServletOutputStream outputStream = response.getOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
        outputStream.close();
    }

    @ApiOperation(value = "Genera una URL del código QR para la 2FA")
    @GetMapping("/v2/generate")
    public ResponseEntity<ResultadoProc<String>> generateQr(Authentication auth, HttpServletResponse response) {
        ResultadoProc.Builder<String> salida = new ResultadoProc.Builder<String>();
        final GoogleAuthenticatorKey key = gAuth.createCredentials(auth.getName());
        String otpAuthURL = GoogleAuthenticatorQRGenerator.getOtpAuthTotpURL("El Desquite", auth.getName(), key);
        return new ResponseEntity<ResultadoProc<String>>(salida.exitoso(otpAuthURL), HttpStatus.OK);
    }

    @ApiOperation(value = "Verifica si la clave 2FA es válida", notes = ""
            + "Valida la clave 2FA entregada por la aplicación Authy, para la correcta verificación del código el usuario debió previemente verificar sus credenciales <a target=\"_blank\" href=\"/swagger-ui.html#/two-factor-authentication-controller/validateKeyUsingPOST\">AQUÍ</a>", responseContainer = "dasdasd")
    @PostMapping("/free/validate/key")
    public ResponseEntity<ResultadoProc<Boolean>> validateKey(@RequestBody Credential2FA credentials) {
        ResultadoProc.Builder<Boolean> salida = new ResultadoProc.Builder<Boolean>();
        Usuario usuario = this.usuarioService.findByEmail(credentials.getUsuario()).getResultado();
        if (usuario == null) {
            log.warn("validateKey() El usuario " + credentials.getUsuario() + " no existe");
            return new ResponseEntity<ResultadoProc<Boolean>>(salida.fallo("Error al validar el token"), HttpStatus.OK);
        }
        if (!usuario.isValidatedLogin()) {
            // si el usuario aún no ha pasado por el login, le mostramos un error
            log.warn("validateKey() El usuario " + credentials.getUsuario() + " no se ha logeado");
            return new ResponseEntity<ResultadoProc<Boolean>>(salida.fallo("Error al validar el token"), HttpStatus.OK);
        }

        Boolean isValidKey = gAuth.authorizeUser(credentials.getUsuario(), Integer.parseInt(credentials.getCode()));
        salida.exitoso(isValidKey, isValidKey ? "Token válido" : "Token inválido");

        if (isValidKey) {
            // 1. Indicamos que la validación de 2FA esta correcta
            usuario.setValidated2Fa(true);
            this.usuarioService.update(usuario);
        }
        return new ResponseEntity<ResultadoProc<Boolean>>(salida.build(), HttpStatus.OK);
    }

    // @GetMapping("/scratches/{username}")
    // public List<Integer> getScratches(@PathVariable String username) {
    // return getScratchCodes(username);
    // }

    // private List<Integer> getScratchCodes(@PathVariable String username) {
    // return credentialRepository.getUser(username).getScratchCodes();
    // }

    // @PostMapping("/scratches/")
    // public Validation validateScratch(@RequestBody Validate2FACode body) {
    // List<Integer> scratchCodes = getScratchCodes(body.getUsername());
    // Validation validation = new
    // Validation(scratchCodes.contains(body.getCode()));
    // scratchCodes.remove(body.getCode());
    // return validation;
    // }
}
