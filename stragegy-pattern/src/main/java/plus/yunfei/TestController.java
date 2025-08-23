package plus.yunfei;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import plus.yunfei.pay.PayService;
import plus.yunfei.pay.WxPayService;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
public class TestController {

    private Map<PayType, PayService> payServiceMap;

    @Autowired
    private WxPayService wxPayService;

    @RequestMapping("/")
    public void test(String payType) {
        PayType userType = PayType.typeOf(payType);
        PayService payService = payServiceMap.getOrDefault(userType, wxPayService);
        payService.pay();
    }

    @Autowired
    public void setPayServiceMap(List<PayService> payServiceList) {
        this.payServiceMap = payServiceList.stream()
                .filter(customerService -> customerService.getClass().isAnnotationPresent(SupportPayType.class))
                .collect(Collectors.toMap(this::findPayTypeFromService, Function.identity()));
        if (this.payServiceMap.size() != PayType.values().length) {
            throw new IllegalArgumentException("缺少支付策略");
        }
    }

    private PayType findPayTypeFromService(PayService customerService) {
        SupportPayType annotation = customerService.getClass().getAnnotation(SupportPayType.class);
        return annotation.value();
    }
}
