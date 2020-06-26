package com.amlo.shopping.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;

import com.amlo.shopping.App;
import com.amlo.shopping.dao.Cart;
import com.amlo.shopping.dao.CartDao;
import com.amlo.shopping.dao.DaoSession;
import com.amlo.shopping.dao.Item;
import com.amlo.shopping.dao.ItemDao;
import com.amlo.shopping.dao.JoinItem;
import com.amlo.shopping.dao.JoinItemDao;
import com.amlo.shopping.dao.Order;
import com.amlo.shopping.dao.OrderDao;
import com.amlo.shopping.dao.User;
import com.amlo.shopping.dao.UserDao;
import com.amlo.shopping.util.AuthenticatorUtil;
import com.example.amloshoppingapp.R;
import com.google.android.gms.common.util.Strings;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Order Placement Activity
 */
public class OrderPlacementActivity extends AppCompatActivity {

    private EditText name, addressLine1, addressLine2, locality, state;
    private RadioButton addressButton;
    private RadioGroup radioGroup;
    private String valOfName, valOfAddress1, valOfAddress2, valOfLocatlity, valOfState;
    private Button orderButton;
    private ScrollView scroller;
    private LinearLayout shippingDetails;
    private DaoSession daoSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        daoSession = ((App)getApplication()).getDaoSession();
        setContentView(R.layout.activity_order_placement);
        radioGroup = findViewById(R.id.same_or_differentAddress);
        String userId = AuthenticatorUtil.getUserId(getApplication());
        orderButton = findViewById(R.id.order_button);
        CartDao cartDao = daoSession.getCartDao();
        Cart cart = cartDao.load(userId);
        if (cart == null || cart.getItem().isEmpty()) {
            orderButton.setVisibility(View.INVISIBLE);
        } else {
            orderButton.setVisibility(View.VISIBLE);
        }

        // Radio group toggle for delivery address
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            scroller = findViewById(R.id.shipping_address);
            if (checkedId == R.id.different_address) {
                scroller.setVisibility(View.VISIBLE);
            } else {
                scroller.setVisibility(View.INVISIBLE);
            }
        });

        // Place order button logic
        orderButton.setOnClickListener(v -> {
            extractValuesOfUser();
            boolean shouldValidateData = (radioGroup.getCheckedRadioButtonId() != R.id.same_address);
            if (!shouldValidateData || validateData()) {
                removeFromCartAndInsertToOrders(userId, shouldValidateData);
                //TODO : Send email
                Intent intent = new Intent(this, PlacedOrderActivity.class);
                startActivity(intent);
            }
        });

    }

    /**
     * Remove items from cart table and insert into order table
     * @param userId
     * @param shouldValidateData
     */
    private void removeFromCartAndInsertToOrders(String userId, boolean shouldValidateData) {
        CartDao cartDao = daoSession.getCartDao();
        ItemDao itemDao = daoSession.getItemDao();
        JoinItemDao joinItemDao = daoSession.getJoinItemDao();

        long orderTime = System.currentTimeMillis();

        Cart cart = cartDao.load(userId);
        cart.resetItem();
        List<Item> items = cart.getItem();


        OrderDao orderDao = daoSession.getOrderDao();
        Order order = new Order();
        order.setOrderTime(orderTime);
        order.setId(userId + "_" + orderTime);
        order.setUserId(userId);
        order.setDeliveryAddress(
                shouldValidateData? getAddressFromUser(userId, daoSession.getUserDao()) : getAddressFromForm()
        );
        orderDao.insert(order);

        order.resetItem();
        //Remove and remap items
        items = items.stream().map(i -> {
            Item item = new Item();
            item.setId(order.getId() + "_" + i.getProductId());
            item.setProductId(i.getProduct().getUniqId());
            item.setProduct(i.getProduct());
            item.setQuantity(i.getQuantity());
            itemDao.delete(i);
            return item;
        }).collect(Collectors.toList());

        items.forEach(i -> {
            itemDao.insert(i);
            JoinItem joinItem = new JoinItem();
            joinItem.setCartOrOrderId(order.getId());
            joinItem.setItemId(i.getId());
            joinItemDao.insert(joinItem);
        });

        List<JoinItem> itemsToDelete =
                joinItemDao.queryBuilder().where(JoinItemDao.Properties.CartOrOrderId.eq(userId)).list();
        itemsToDelete.forEach(joinItemDao::delete);

        cart.resetItem();
        cartDao.update(cart);
    }

    /**
     * GEt shipping address from user profile
     * @param userId
     * @param userDao
     * @return
     */
    private String getAddressFromUser(String userId, UserDao userDao) {
        User user = userDao.load(userId);
        userDao.detach(user);
        return user.getAddress();
    }

    /**
     * Get Address from Address Form (User gave alternate address)
     * @return
     */
    private String getAddressFromForm() {
        StringBuilder sb = new StringBuilder();
        if (!Strings.isEmptyOrWhitespace(valOfAddress1)) {
            sb.append(valOfAddress1);
            sb.append(",");
        }

        if (!Strings.isEmptyOrWhitespace(valOfAddress2)) {
            sb.append(valOfAddress2);
            sb.append(",");
        }

        if (!Strings.isEmptyOrWhitespace(valOfLocatlity)) {
            sb.append(valOfLocatlity);
            sb.append(",");
        }

        if (!Strings.isEmptyOrWhitespace(valOfState)) {
            sb.append(valOfState);
        }

        return sb.toString();
    }

    /**
     * Reset form
     */
    public void resetValues() {
        name.setText("");
        addressLine1.setText("");
        addressLine2.setText("");
        locality.setText("");
        state.setText("");
    }

    /**
     * Extract values from form
     */
    public void extractValuesOfUser() {
        name = findViewById(R.id.delivery_customer_name);
        valOfName = name.getText().toString();
        addressLine1 = findViewById(R.id.delivery_address_line_1);
        valOfAddress1 = addressLine1.getText().toString();
        addressLine2 = findViewById(R.id.delivery_address_line_2);
        valOfAddress2 = addressLine2.getText().toString();
        locality = findViewById(R.id.delivery_address_locality);
        valOfLocatlity = locality.getText().toString();
        state = findViewById(R.id.delivery_address_state);
        valOfState = state.getText().toString();
    }

    /**
     * Validate data in address form
     * @return
     */
    public boolean validateData() {
        boolean isValid = true;
        if (valOfName.isEmpty()) {
            name.setError("Please Enter Name");
            isValid = false;
        } else if (valOfName.length() > 50) {
            name.setError("Length cannot exceed Limit");
            isValid = false;
        }

        if (valOfAddress1.isEmpty()) {
            addressLine1.setError("Please Enter Address");
            isValid = false;
        } else if (valOfAddress1.length() > 50) {
            addressLine1.setError("Length cannot exceed Limit");
            isValid = false;
        }

        if (valOfAddress2.isEmpty()) {
            addressLine2.setError("Please Enter Address");
            isValid = false;
        } else if (valOfAddress2.length() > 50) {
            addressLine2.setError("Length cannot exceed Limit");
            isValid = false;
        }

        if (valOfLocatlity.isEmpty()) {
            locality.setError("Please Enter Locality");
            isValid = false;
        } else if (valOfLocatlity.length() > 50) {
            locality.setError("Length cannot exceed Limit");
            isValid = false;
        }

        if (valOfState.isEmpty()) {
            state.setError("Please Enter State");
            isValid = false;
        } else if (valOfState.length() > 50) {
            state.setError("Length cannot exceed Limit");
            isValid = false;
        }
        return isValid;
    }
}
