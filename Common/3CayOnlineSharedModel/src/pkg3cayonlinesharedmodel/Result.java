/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3cayonlinesharedmodel;

/**
 *
 * @author huynguyen
 */
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class Result<T> {
    private final Optional<T> value;
    private final Optional<String> error;

    private Result(T value, String error) {
        this.value = Optional.ofNullable(value);
        this.error = Optional.ofNullable(error);
    }

    public static <S> Result<S> ok(S value) {
        return new Result<>(value, null);
    }

    public static <R> Result<R> error(String errorStr) {
        return new Result<>(null, errorStr);
    }
    
    public<U> Result<U> map(Function<? super T, U> mapper) {
        return Result.ok(mapper.apply(value()));
    }

    public void ifError(Consumer<String> consumer) {
        if(isError()) {
            consumer.accept(errorVal());
        }
    }
    
    public void either(Consumer<? super T> left, Consumer<String> right) {
        if(isError()) {
            right.accept(errorVal());
        } else {
            left.accept(value());
        }
           
    }

    public<U> Result<U> flatMap(Function<? super T, Result<U>> mapper) {

        if(this.isError()) {
            return error(this.errorVal());
        }

        return mapper.apply(value.get());
    }

    public String errorVal() {
        return error.get();
    }

    public boolean isError() {
        return error.isPresent();
    }

    public T value() {
        return value.get();
    }

    @Override
    public String toString() {
        String result = value.isPresent() ? value.get().toString() : "Error: " + error.get().toString();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Result<?> result = (Result<?>) o;

        if (!value.equals(result.value)) return false;
        return error.equals(result.error);

    }

    @Override
    public int hashCode() {
        int result = value.hashCode();
        result = 31 * result + error.hashCode();
        return result;
    }
}