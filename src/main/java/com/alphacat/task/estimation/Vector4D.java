package com.alphacat.task.estimation;

import com.alphacat.pojo.SquareTag;
import com.alphacat.vo.SquareVO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Vector4D {

    int a, b, c, d;

    Vector4D(SquareVO s) {
        this(s.getX(), s.getY(), s.getW(), s.getH());
    }

    Vector4D(SquareTag s) {
        this(s.getX(), s.getY(), s.getW(), s.getH());
    }

    Vector4D diff(Vector4D v) {
        return new Vector4D(a - v.a, b - v.b, c - v.c, d - v.d);
    }

    double product(Vector4D v) {
        return a * v.a + b * v.b + c * v.c + d * v.d;
    }

    double msq() {
        return this.product(this);
    }

    double mag() {
        return Math.sqrt(this.msq());
    }

}
