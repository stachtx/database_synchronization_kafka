package com.centralserver.controllers;

import com.centralserver.exception.base.SystemBaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/secured/revoke/token")
@Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = SystemBaseException.class)
public class RevokeTokenController {

    @Autowired
    private ConsumerTokenServices customerTokenService;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public HttpStatus revokeToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.contains("Bearer")){
            String tokenId = token.substring("Bearer".length()+1);
            customerTokenService.revokeToken(tokenId);
            return HttpStatus.ACCEPTED;
        }
        return HttpStatus.BAD_REQUEST;
    }
}
