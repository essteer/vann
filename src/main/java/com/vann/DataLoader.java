package com.vann;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.vann.model.Category;
import com.vann.model.Customer;
import com.vann.model.Invoice;
import com.vann.model.InvoiceItem;
import com.vann.model.Product;
import com.vann.model.Cart;
import com.vann.model.CartItem;
import com.vann.model.enums.CategoryType;
import com.vann.model.enums.Colour;
import com.vann.model.enums.Size;
import com.vann.repositories.CategoryRepo;
import com.vann.repositories.CustomerRepo;
import com.vann.repositories.InvoiceItemRepo;
import com.vann.repositories.InvoiceRepo;
import com.vann.repositories.ProductRepo;
import com.vann.repositories.CartItemRepo;
import com.vann.repositories.CartRepo;
import com.vann.service.CategoryService;
import com.vann.service.CustomerService;
import com.vann.service.InvoiceItemService;
import com.vann.service.InvoiceService;
import com.vann.service.ProductService;
import com.vann.service.CartItemService;
import com.vann.service.CartService;

@Component
public class DataLoader implements ApplicationRunner {

    private CategoryRepo categoryRepo;
    private CustomerRepo customerRepo;
    private InvoiceRepo invoiceRepo;
    private InvoiceItemRepo invoiceItemRepo;
    private ProductRepo productRepo;
    private CartRepo cartRepo;
    private CartItemRepo cartItemRepo;
    

    public DataLoader(CategoryRepo categoryRepo, CustomerRepo customerRepo, InvoiceRepo invoiceRepo, InvoiceItemRepo invoiceItemRepo, ProductRepo productRepo, CartRepo cartRepo, CartItemRepo cartItemRepo) {
        super();
        this.categoryRepo = categoryRepo;
        this.customerRepo = customerRepo;
        this.invoiceRepo = invoiceRepo;
        this.invoiceItemRepo = invoiceItemRepo;
        this.productRepo = productRepo;
        this.cartRepo = cartRepo;
        this.cartItemRepo = cartItemRepo;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        CategoryService categoryService = new CategoryService(this.categoryRepo);
        CustomerService customerService = new CustomerService(this.customerRepo);
        InvoiceService invoiceService = new InvoiceService(this.invoiceRepo);
        InvoiceItemService invoiceItemService = new InvoiceItemService(this.invoiceItemRepo);
        ProductService productService = new ProductService(this.productRepo);
        CartService cartService = new CartService(this.cartRepo);
        CartItemService cartItemService = new CartItemService(this.cartItemRepo);


        Category bracelet = new Category();
        bracelet.setCategoryType(CategoryType.BRACELET);
        bracelet.setCategoryName("bracelets");

        Category earcuff = new Category();
        earcuff.setCategoryType(CategoryType.EARRING);
        earcuff.setCategoryName("earcuffs");

        Category earring = new Category();
        earring.setCategoryType(CategoryType.EARRING);
        earring.setCategoryName("earrings");

        Category necklace = new Category();
        necklace.setCategoryType(CategoryType.NECKLACE);
        necklace.setCategoryName("necklaces");

        Category ring = new Category();
        ring.setCategoryType(CategoryType.RING);
        ring.setCategoryName("rings");

        categoryService.saveCategory(bracelet);
        categoryService.saveCategory(earcuff);
        categoryService.saveCategory(earring);
        categoryService.saveCategory(necklace);
        categoryService.saveCategory(ring);

        for (Category category : categoryService.findAllCategories()) {
            System.out.println(category.toString());
        }
        System.out.println();


        Product xlMoltenEarrings = new Product();
        xlMoltenEarrings.setCategory(earring);
        xlMoltenEarrings.setProductName("XL MOLTEN EARRINGS");
        xlMoltenEarrings.setProductPrice(575);
        xlMoltenEarrings.setProductImage("/files/XL_Molten_F.jpg?v=1706969778");
        xlMoltenEarrings.setColour(Colour.SILVER);

        Product heartEarCuff_WhiteGold = new Product();
        heartEarCuff_WhiteGold.setCategory(earcuff);
        heartEarCuff_WhiteGold.setProductName("HEART EAR CUFF");
        heartEarCuff_WhiteGold.setProductPrice(105);
        heartEarCuff_WhiteGold.setProductImage("/files/HEART_EAR_CUFF_WG.jpg?v=1706532715");
        heartEarCuff_WhiteGold.setColour(Colour.WHITE_GOLD);

        Product lavaRingWithDropPendant_US_06 = new Product();
        lavaRingWithDropPendant_US_06.setCategory(ring);
        lavaRingWithDropPendant_US_06.setProductName("LAVA RING");
        lavaRingWithDropPendant_US_06.setProductPrice(320);
        lavaRingWithDropPendant_US_06.setProductImage("/files/LAVA_RING_WITH_DROP_PENDANT.jpg?v=1706619989");
        lavaRingWithDropPendant_US_06.setColour(Colour.WHITE_GOLD);
        lavaRingWithDropPendant_US_06.setSize(Size.US_05); // intentional error to update to US_06

        Product novaBracelet = new Product();
        novaBracelet.setCategory(bracelet);
        novaBracelet.setProductName("NOVA BRACELET");
        novaBracelet.setProductPrice(365);
        novaBracelet.setProductImage("/products/Nova_Bracelet.jpg?v=1707045353");
        novaBracelet.setColour(Colour.WHITE_AND_YELLOW_GOLD);

        Product pearlChokerNecklace_Heather = new Product();
        pearlChokerNecklace_Heather.setCategory(necklace);
        pearlChokerNecklace_Heather.setProductName("PEARL CHOKER NECKLACE");
        pearlChokerNecklace_Heather.setProductPrice(315);
        pearlChokerNecklace_Heather.setProductImage("/files/Pearl_chocker_Heather_WG.jpg?v=1706869241");
        pearlChokerNecklace_Heather.setColour(Colour.HEATHER);

        productService.saveProduct(heartEarCuff_WhiteGold);
        productService.saveProduct(lavaRingWithDropPendant_US_06);
        productService.saveProduct(novaBracelet);
        productService.saveProduct(pearlChokerNecklace_Heather);
        productService.saveProduct(xlMoltenEarrings);

        for (Product product : productService.findAllProducts()) {
            System.out.println(product.toString());
        }
        System.out.println();

        UUID lavaRingWithDropPendant_US_06_Uuid = lavaRingWithDropPendant_US_06.getProductUuid();

        Product updated_lavaRingWithDropPendant_US_06 = new Product();
        updated_lavaRingWithDropPendant_US_06.setCategory(ring);
        updated_lavaRingWithDropPendant_US_06.setProductName("LAVA RING");
        updated_lavaRingWithDropPendant_US_06.setProductPrice(320);
        updated_lavaRingWithDropPendant_US_06.setProductImage("/files/LAVA_RING_WITH_DROP_PENDANT.jpg?v=1706619989");
        updated_lavaRingWithDropPendant_US_06.setColour(Colour.WHITE_GOLD);
        updated_lavaRingWithDropPendant_US_06.setSize(Size.US_06);

        productService.updateProduct(lavaRingWithDropPendant_US_06_Uuid, updated_lavaRingWithDropPendant_US_06);
        Optional<Product> lavaRingOpt = productService.findProductById(lavaRingWithDropPendant_US_06_Uuid);
        lavaRingOpt.ifPresent(lavaRing -> {
            System.out.println(lavaRingOpt.toString());;
        });


        Customer demoCustomer1 = new Customer();
        demoCustomer1.setCustomerName("Anon Ymous");
        demoCustomer1.setCustomerEmail("anon@ymous.com");
        
        Customer demoCustomer2 = new Customer();
        demoCustomer2.setCustomerName("Some One");
        demoCustomer2.setCustomerEmail("some@one.com");
        
        customerService.saveCustomer(demoCustomer1);
        customerService.saveCustomer(demoCustomer2);

        for (Customer customer : customerService.findAllCustomers()) {
            System.out.println(customer.toString());
        }

        UUID demoCustomer2Uuid = demoCustomer2.getCustomerUuid();
        Optional<Customer> demoCust2Opt = customerService.findCustomerById(demoCustomer2Uuid);
        demoCust2Opt.ifPresent(demoCust2 -> {
            demoCust2.setCustomerName("Some Person");
            demoCust2.setCustomerEmail("person@some.com");
            customerService.saveCustomer(demoCust2);
        });

        for (Customer customer : customerService.findAllCustomers()) {
            System.out.println(customer.toString());
        }

        customerService.deleteCustomer(demoCustomer2Uuid);

        for (Customer customer : customerService.findAllCustomers()) {
            System.out.println(customer.toString());
        }


        Cart cart = new Cart();
        cart.setCartCustomer(demoCustomer1);
        cartService.saveCart(cart);

        UUID cartUuid = cart.getCartUuid();
        Optional<Cart> cartOpt = cartService.findCartById(cartUuid);
        if (cartOpt.isPresent()) {
            Cart concreteCart = cartOpt.get();
            Set<CartItem> items = new HashSet<>();

            CartItem cartItem1 = new CartItem(concreteCart, xlMoltenEarrings, 2);
            CartItem cartItem2 = new CartItem(concreteCart, pearlChokerNecklace_Heather, 1);
            items.add(cartItem1);
            items.add(cartItem2);
            concreteCart.setCartItems(items);
            cartService.saveCart(concreteCart);
        }

        for (Cart c : cartService.findAllCarts()) {
            System.out.println(c);
        }

        cartService.checkoutCart(cartUuid, "17 Queens Road East, Central, Hong Kong", cartItemService);

        // CartService.deleteCart(cartUuid);

        // for (Cart cart : CartService.findAllCarts()) {
        //     System.out.println(cart);
        // }


        Invoice invoice = new Invoice();
        invoice.setCustomer(demoCustomer1);
        invoice.setBillingAddress("17 Queens Road East, Central, Hong Kong");
        invoice.setShippingAddress("17 Queens Road East, Central, Hong Kong");

        InvoiceItem invoiceItem1 = new InvoiceItem(invoice, xlMoltenEarrings, 2, xlMoltenEarrings.getProductPrice());
        InvoiceItem invoiceItem2 = new InvoiceItem(invoice, pearlChokerNecklace_Heather, 1, pearlChokerNecklace_Heather.getProductPrice());
        
        invoiceItemService.saveInvoiceItem(invoiceItem1);
        invoiceItemService.saveInvoiceItem(invoiceItem2);
        
        List<InvoiceItem> invoiceItemList = invoice.getInvoiceItems();
        invoiceItemList.add(invoiceItem1);
        invoiceItemList.add(invoiceItem2);
        invoice.setInvoiceItems(invoiceItemList);
        invoice.calculateTotalAmount();
        invoiceService.saveInvoice(invoice);
        
        for (Invoice inv : invoiceService.findAllInvoices()) {
            System.out.println(inv.toString());
        }
        System.out.println();

        List<Cart> carts = cartService.findAllCarts();
        for (Cart c : carts) {
            UUID id = c.getCartUuid();
            // deletes Carts and associated invoice products
            cartService.deleteCart(id, cartItemService);
        }

        List<Product> products = productService.findAllProducts();
        for (Product product : products) {
            UUID id = product.getProductUuid();
            productService.deleteProduct(id);
        }

        List<Category> categories = categoryService.findAllCategories();
        for (Category category : categories) {
            UUID id = category.getCategoryUuid();
            // deleting categories does NOT delete associated products
            categoryService.deleteCategory(id);
        }

        List<Invoice> invoices = invoiceService.findAllInvoices();
        for (Invoice inv : invoices) {
            UUID id = inv.getInvoiceUuid();
            // deletes invoices and associated invoice products
            invoiceService.deleteInvoice(id, invoiceItemService);
        }

        List<Customer> customers = customerService.findAllCustomers();
        for (Customer customer : customers) {
            UUID id = customer.getCustomerUuid();
            customerService.deleteCustomer(id);
        }
    }

}