package com.lambdaschool.shoppingcart.services;

import com.lambdaschool.shoppingcart.ShoppingCartApplication;
import com.lambdaschool.shoppingcart.models.CartItem;
import com.lambdaschool.shoppingcart.models.CartItemId;
import com.lambdaschool.shoppingcart.models.Product;
import com.lambdaschool.shoppingcart.models.User;
import com.lambdaschool.shoppingcart.repository.CartItemRepository;
import com.lambdaschool.shoppingcart.repository.ProductRepository;
import com.lambdaschool.shoppingcart.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShoppingCartApplication.class, properties={"command.line.runner.enabled = false"})
public class CartItemServiceImplUnitTest {
    @Autowired
    private CartItemService cartitemserv;

    @MockBean
    private UserRepository userrepo;

    @MockBean
    private ProductRepository prodrepo;

    @MockBean
    private CartItemRepository cartitemrepo;

    User user = new User();
    List<Product> prodlist = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        Product p1 = new Product();
        Product p2 = new Product();
        p1.setName("Shirt");
        p1.setPrice(25.50);
        p1.setProductid(22);
        p2.setName("Skirt");
        p2.setPrice(15);
        p2.setProductid(23);

        prodlist.add(p1);
        prodlist.add(p2);

        User u1 = new User();
        u1.setUsername("furby");
        u1.setPasswordNoEncrypt("testpass");
        u1.setUserid(20);

        u1.getCarts().add(new CartItem(u1, p1, 3, ""));
        u1.getCarts().add(new CartItem(u1, p1, 1, ""));

        user = u1;

        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void addToCart() {
        CartItemId cartid = new CartItemId(user.getUserid(), prodlist.get(0).getProductid());
        Mockito.when(userrepo.findById(10L))
                .thenReturn(Optional.of(user));
        Mockito.when(prodrepo.findById(10L))
                .thenReturn(Optional.of(prodlist.get(0)));
        Mockito.when(cartitemrepo.findById(cartid))
                .thenReturn(Optional.of(new CartItem(user, prodlist.get(0), 4L, "")));
        cartitemrepo.save(new CartItem(user, prodlist.get(0), 4L, ""));
    }

    @Test
    public void removeFromCart() {
        CartItemId cartid = new CartItemId(user.getUserid(), prodlist.get(0).getProductid());
        CartItem cartitem = new CartItem(user, prodlist.get(0), 4L, "");
        Mockito.when(userrepo.findById(10L))
                .thenReturn(Optional.of(user));
        Mockito.when(prodrepo.findById(10L))
                .thenReturn(Optional.of(prodlist.get(1)));
        Mockito.when(cartitemrepo.findById(cartid))
                .thenReturn(Optional.of(cartitem));
        Mockito.doNothing().when(cartitemrepo).deleteById(cartid);
        cartitemrepo.save(cartitem);
    }
}