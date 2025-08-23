package plus.yunfei.pay;

import org.springframework.stereotype.Component;
import plus.yunfei.PayType;
import plus.yunfei.SupportPayType;

@Component
@SupportPayType(PayType.ALI)
public class AliPayService implements PayService {


    @Override
    public void pay() {

    }
}