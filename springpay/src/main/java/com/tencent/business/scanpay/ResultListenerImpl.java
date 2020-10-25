package com.tencent.business.scanpay;

import com.tencent.business.ScanPayBusiness.ResultListener;
import com.tencent.protocol.pay_protocol.ScanPayResData;

public class ResultListenerImpl implements ResultListener {

	@Override
	public void onFailByReturnCodeError(ScanPayResData scanPayResData) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFailByReturnCodeFail(ScanPayResData scanPayResData) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFailBySignInvalid(ScanPayResData scanPayResData) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFailByAuthCodeExpire(ScanPayResData scanPayResData) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFailByAuthCodeInvalid(ScanPayResData scanPayResData) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFailByMoneyNotEnough(ScanPayResData scanPayResData) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFail(ScanPayResData scanPayResData) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccess(ScanPayResData scanPayResData) {
		// TODO Auto-generated method stub

	}

}
