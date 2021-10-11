package web.converter;

import core.domain.CustomerPurchasePrimaryKey;
import core.domain.Purchase;
import org.springframework.stereotype.Component;
import web.dto.PurchaseDTO;

@Component
public class PurchaseConverter extends BaseConverter<CustomerPurchasePrimaryKey, Purchase, PurchaseDTO> {
    @Override
    public Purchase convertDtoToModel(PurchaseDTO dto) {
        var model = new Purchase();
        model.setId(dto.getId());
        model.setCat(dto.getCat());
        model.setCustomer(dto.getCustomer());
        model.setDateAcquired(dto.getDateAcquired());
        model.setPrice(dto.getPrice());
        model.setReview(dto.getReview());
        return model;
    }

    @Override
    public PurchaseDTO convertModelToDto(Purchase purchase) {
        var purchaseDTO = new PurchaseDTO();
        purchaseDTO.setId(purchase.getId());
        purchaseDTO.setCat(purchase.getCat());
        purchaseDTO.setCustomer(purchase.getCustomer());
        purchaseDTO.setDateAcquired(purchase.getDateAcquired());
        purchaseDTO.setPrice(purchase.getPrice());
        purchaseDTO.setReview(purchase.getReview());
        return purchaseDTO;
    }
}
