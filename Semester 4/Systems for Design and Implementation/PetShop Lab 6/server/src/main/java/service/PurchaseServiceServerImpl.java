package service;

import common.domain.Cat;
import common.domain.Customer;
import common.domain.Pair;
import common.domain.Purchase;
import common.exceptions.PetShopException;
import common.service.IPurchaseService;
import repository.IRepository;

import java.util.*;
import java.util.stream.Collectors;

public class PurchaseServiceServerImpl implements IPurchaseService {
    final IRepository<Pair<Long, Long>, Purchase> purchaseRepository;
    final IRepository<Long, Cat> catsRepository;
    final IRepository<Long, Customer> customerRepository;

    public PurchaseServiceServerImpl(IRepository<Pair<Long, Long>, Purchase> purchaseRepository, IRepository<Long, Cat> catsRepository, IRepository<Long, Customer> customerRepository) {
        this.purchaseRepository = purchaseRepository;
        this.catsRepository = catsRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public void addPurchase(Long catId, Long customerId, int price, Date dateAcquired, int review) {
        List<Long> catIds = new ArrayList<>();
        purchaseRepository.findAll().forEach(purchase -> catIds.add(purchase.getCatId()));
        catIds.stream().filter(cat -> cat.equals(catId)).findAny().ifPresent(cat -> {
            throw new PetShopException("The cat is already purchased");
        });
        Optional<Cat> cat = catsRepository.findOne(catId);
        cat.ifPresentOrElse((Cat c) -> {
            Optional<Customer> customer = customerRepository.findOne(customerId);
            customer.ifPresentOrElse((Customer cust) -> {
                Purchase purchase = new Purchase(catId, customerId, price, dateAcquired, review);
                purchaseRepository.save(purchase);
            }, () -> {
                throw new PetShopException("Customer id does not exist");
            });
        }, () -> {
            throw new PetShopException("Cat id does not exist");
        });
    }

    @Override
    public Set<Purchase> getPurchasesFromRepository() {
        return (Set<Purchase>) purchaseRepository.findAll();
    }


    @Override
    public void deletePurchase(Long catId, Long customerId) {
        purchaseRepository.delete(new Pair<>(catId, customerId))
                .orElseThrow(() -> new PetShopException("Purchase does not exist"));
    }

    @Override
    public void updatePurchase(Long catId, Long customerId, int newReview) {
        catsRepository.findOne(catId).orElseThrow(() -> new PetShopException("Cat does not exist"));
        customerRepository.findOne(customerId).orElseThrow(() -> new PetShopException("Customer does not exist"));
        Purchase purchase = purchaseRepository.findOne(new Pair<>(catId, customerId))
                .orElseThrow(() -> new PetShopException("Purchase does not exist"));
        purchaseRepository.update(new Purchase(catId, customerId, purchase.getPrice(), purchase.getDateAcquired(), newReview));
    }

    @Override
    public List<Customer> filterCustomersThatBoughtBreedOfCat(String breed) {
        return  ((Set<Customer>) customerRepository.findAll()).stream()
                .filter((customer) ->
                        getPurchasesFromRepository().stream().anyMatch((purchase) ->
                                ((Set<Cat>) catsRepository.findAll()).stream().anyMatch((cat) ->
                                        purchase.getCatId().equals(cat.getId()) && purchase.getCustomerId().equals(customer.getId()) && cat.getBreed().equals(breed))))
                .collect(Collectors.toList());
    }

    @Override
    public List<Purchase> filterPurchasesWithMinStars(int minStars) {
        return getPurchasesFromRepository().stream()
                .filter(purchase -> purchase.getReview() >= minStars)
                .collect(Collectors.toList());
    }

    @Override
    public List<Pair<Customer, Integer>> reportCustomersSortedBySpentCash() {
        List<Pair<Customer, Integer>> toReturn = new ArrayList<>();
        customerRepository.findAll().forEach((customer) -> {
            int moneySpent = getPurchasesFromRepository().stream()
                    .filter(purchase -> purchase.getCustomerId().equals(customer.getId()))
                    .map(Purchase::getPrice)
                    .reduce(0, Integer::sum);
            toReturn.add(new Pair<>(customer, moneySpent));
        });
        toReturn.sort((p1, p2) -> -p1.getRight().compareTo(p2.getRight()));
        return toReturn;
    }
}
