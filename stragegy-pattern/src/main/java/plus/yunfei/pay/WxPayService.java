package plus.yunfei.pay;

import org.springframework.stereotype.Component;
import plus.yunfei.PayType;
import plus.yunfei.SupportPayType;

@Component
@SupportPayType(PayType.WX)
public class WxPayService implements PayService {


    @Override
    public void pay() {

    }
}