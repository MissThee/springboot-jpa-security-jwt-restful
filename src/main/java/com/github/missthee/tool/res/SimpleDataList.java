package com.github.missthee.tool.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SimpleDataList<T> {
    private Collection<T> dataList;
    private Long total;
}
