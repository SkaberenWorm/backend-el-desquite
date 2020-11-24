package cl.desquite.backend.utils;

import java.io.Serializable;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;

@Data
@NoArgsConstructor
@AllArgsConstructor
@CommonsLog
public class SearchPagination<T> implements Serializable {

    private static final long serialVersionUID = 266254741211494848L;
    private T seek;
    private int page;
    private int records;
    private String order;
    private String direction;

    /**
     * @param filtro
     * @param page
     * @param records
     */
    public SearchPagination(T filtro, int page, int records) {
        super();
        this.seek = filtro;
        this.page = page;
        this.records = records;
    }

    public int getPage() {
        return this.page - 1;
    }

    public Direction getDirection() {
        if (this.direction != null && !this.direction.isEmpty()) {
            return Direction.fromString(this.direction);
        }
        return Direction.ASC;
    }

    public PageRequest getPageRequest() {
        try {
            if (this.direction == null || this.order == null || (this.order != null && this.order.length() == 0)) {
                return PageRequest.of(this.getPage(), this.getRecords());
            } else {
                return PageRequest.of(this.getPage(), this.getRecords(), this.getDirection(), this.order);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }

    }

}