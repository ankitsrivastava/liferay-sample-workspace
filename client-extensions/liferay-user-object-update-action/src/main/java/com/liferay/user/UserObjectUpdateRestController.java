/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.user;

import com.liferay.portal.search.rest.client.dto.v1_0.Suggestion;
import com.liferay.portal.search.rest.client.dto.v1_0.SuggestionsContributorConfiguration;
import com.liferay.portal.search.rest.client.dto.v1_0.SuggestionsContributorResults;
import com.liferay.portal.search.rest.client.pagination.Page;
import com.liferay.portal.search.rest.client.resource.v1_0.SuggestionResource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author Ankit Srivastava
 * Summary: Sample to execute custom logic on user object update event. 
 * A workaround to achieve post login action as user lastlogindate get modified every time when they log in.
 */


@RestController
public class UserObjectUpdateRestController extends BaseRestController {


	/**
 	*  Method will be called when user object get modified.
 	*/

	@PostMapping("/object/action/user")
	public ResponseEntity<String> post(
			@AuthenticationPrincipal Jwt jwt, @RequestBody String json)
		throws Exception {

		log(jwt, _log, json);

		JSONObject jsonObject = new JSONObject(json);

		// json object will consist of user details being updated. Fetching needed attributes.

		JSONObject objectEntryUserJSONObject =
			jsonObject.getJSONObject("modelDTOAccount");

		long userId =
		    objectEntryUserJSONObject.getLong("id");

		_log.info("User Id for user being updated is"+userId);
			
		/**
		 * Call to User Role association API. Hardcoding 20099 roleId to be assigned, another call can be made to fetch this value through Headless API. 
        */

		return new ResponseEntity<>(
      WebClient.create(
        lxcDXPServerProtocol + "://" + lxcDXPMainDomain
      ).post(
	  ).uri(
		"o/headless-admin-user/v1.0/roles/20099/association/user-account/"+
	     objectEntryUserJSONObject.getLong("id")
      ).accept(
         MediaType.APPLICATION_JSON
      ).contentType(
         MediaType.APPLICATION_JSON
      ).header(
         HttpHeaders.AUTHORIZATION, "Bearer " + jwt.getTokenValue()
      ).retrieve(
      ).bodyToMono(
         String.class
      ).block(),
      HttpStatus.OK);
	  
}

private static final Log _log = LogFactory.getLog(
	UserObjectUpdateRestController.class);

}