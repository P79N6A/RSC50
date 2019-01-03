package com.synet.tool.rsc.incr.scd;

import com.synet.tool.rsc.compare.Difference;
import com.synet.tool.rsc.incr.BaseConflictHandler;
import com.synet.tool.rsc.io.parser.ParserUtil;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1062PinEntity;
import com.synet.tool.rsc.util.F1011_NO;
import com.synet.tool.rsc.util.Rule;

public class PinConflictHandler extends BaseConflictHandler {

	public PinConflictHandler(Difference diff) {
		super(diff);
		
	}

	@Override
	public void handleAdd() {
		Tb1046IedEntity ied = (Tb1046IedEntity) diff.getParent().getParent().getData();
		Rule type = F1011_NO.OTHERS;
		String pinRef = diff.getName();
		String doDesc = diff.getDesc();
		Tb1062PinEntity pin = ParserUtil.createPin(ied, pinRef , doDesc, type.getId(), 0);
		beanDao.insert(pin);
	}

	@Override
	public void handleDelete() {
	}

	@Override
	public void handleUpate() {
	}


}
