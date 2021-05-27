package web.converter;

import core.domain.CustomerPurchasePrimaryKey;
import core.domain.Purchase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import web.dto.PurchaseDTO;

@Component
public class PurchaseConverter extends BaseConverter<CustomerPurchasePrimaryKey, Purchase, PurchaseDTO> {
    @Autowired
    private CatConverter catConverter;

    @Autowired
    private CustomerConverter customerConverter;

    @Override
    public Purchase convertDtoToModel(PurchaseDTO dto) {
        var model = new Purchase();
        model.setId(dto.getId());
        model.setCat(catConverter.convertDtoToModel(dto.getCat()));
        model.setCustomer(customerConverter.convertDtoToModel(dto.getCustomer()));
        model.setDateAcquired(dto.getDateAcquired());
        model.setPrice(dto.getPrice());
        model.setReview(dto.getReview());
        return model;
    }

    @Override
    public PurchaseDTO convertModelToDto(Purchase purchase) {
        var purchaseDTO = new PurchaseDTO();
        purchaseDTO.setId(purchase.getId());
        purchaseDTO.setCat(catConverter.convertModelToDto(purchase.getCat()));
        purchaseDTO.setCustomer(customerConverter.convertModelToDto(purchase.getCustomer()));
        purchaseDTO.setDateAcquired(purchase.getDateAcquired());
        purchaseDTO.setPrice(purchase.getPrice());
        purchaseDTO.setReview(purchase.getReview());
        return purchaseDTO;
    }
}
