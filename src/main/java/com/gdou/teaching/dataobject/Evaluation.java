package com.gdou.teaching.dataobject;

import lombok.Data;

import java.util.List;

/**
 * @author bo
 * @date Created in 22:45 2020/1/14
 * @description
 **/
@Data
public class Evaluation {
    private String id;
    private String label;
    private String pid;
    private List<Evaluation> children;
}
