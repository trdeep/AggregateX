package cn.treedeep.king.shared;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
public final class Result implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final int code;
    private final String message;
    private final Object data;

    public static Result success() {
        return success(true);
    }

    public static Result success(Object data) {
        return new Result(200, "Success", data);
    }

    public static Result success(String message, Object data) {
        return new Result(200, message, data);
    }

    public static Result failure() {
        return new Result(500, "Failure", null);
    }

    public static Result failure(String message) {
        return new Result(500, message, null);
    }

    public static Result error(int code, String message) {
        return new Result(code, message, null);
    }

    public <T> T getData() {
        return (T) data;
    }
}
