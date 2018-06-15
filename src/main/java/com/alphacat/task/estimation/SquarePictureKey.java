package com.alphacat.task.estimation;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * In square tag tasks estimation(especially for multi-square tag tasks),
 * to simplify its process, we assume one rectangle along with its label data
 * as the data of a '<b>picture</b>', so the key to differentiate a picture,
 * contains both the picture id and the rectangle id, and even with the same
 * picture id, as long as the rectangle ids are different, we say they are
 * different pictures.
 */
@Data
@AllArgsConstructor
class SquarePictureKey {

    /**
     * The picture id in a task.
     */
    private int pid;
    /**
     * The rectangle id of a picture.
     */
    private int rid;

}
