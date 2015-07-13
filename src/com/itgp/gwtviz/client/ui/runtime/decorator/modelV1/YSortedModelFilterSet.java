/*
 */
package com.itgp.gwtviz.client.ui.runtime.decorator.modelV1;

import com.itgp.gwtviz.client.ui.runtime.YSortedModelV1;
import com.itgp.gwtviz.client.ui.runtime.decorator.FilterSet;
import com.itgp.gwtviz.shared.gconfig.GraphConfigUtility;

/**
 <p>
 @author Warp
 */
public class YSortedModelFilterSet extends FilterSet{

    public YSortedModelFilterSet(GraphConfigUtility.YAxisSort direction){
        this.setSorter(new YSortedModelV1Sorter(direction));
    }

    @Override
    public int getModelSize(){
        YSortedModelV1 m = (YSortedModelV1) getModel();
        return m.size();
    }

    public void setModel(YSortedModelV1 model){
        super.setModel(model);
    }
    
    
}
