
package com.mayu.jpetstore.ui;

import com.mayu.jpetstore.component.event.EntityChangedEvent;
import com.mayu.jpetstore.domain.Account;
import com.mayu.jpetstore.domain.Product;
import com.mayu.jpetstore.service.AccountUserDetails;
import com.mayu.jpetstore.service.CatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 */
@Component
@RequiredArgsConstructor
public class EventListeners {

	private CatalogService catalogService;
	private Favourite favourite;

	@EventListener
	public void handleAuthenticationSuccessEvent(AuthenticationSuccessEvent event) {
		Account account = ((AccountUserDetails) event.getAuthentication().getPrincipal()).getAccount();
		loadFavouriteProductList(account);
	}

	@EventListener
	public void handleAccountChangedEvent(EntityChangedEvent<Account> event) {
		Account account = event.getEntity();
		loadFavouriteProductList(account);
	}

	private void loadFavouriteProductList(Account account) {
		if (account.isListOption()) {
			List<Product> productList = catalogService.getProductListByCategory(account.getFavouriteCategoryId());
			favourite.setProductList(productList);
		} else {
			favourite.setProductList(null);
		}
	}

}
