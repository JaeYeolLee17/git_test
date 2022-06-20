package com.e4motion.challenge.common.utils;

import lombok.Data;
import lombok.experimental.Delegate;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Data
public class ValidList<E> implements List<E> {

    @Valid
    @Delegate
    private List<E> list = new ArrayList<>();

}