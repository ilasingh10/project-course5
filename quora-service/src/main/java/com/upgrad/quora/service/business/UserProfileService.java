package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordCryptographyProvider cryptographyProvider;

    public UserEntity getUser(final String userUuid, final String authorizationToken) throws AuthorizationFailedException, UserNotFoundException {

        UserAuthEntity userAuthEntity = userDao.getUserAuth(authorizationToken);
        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        UserEntity userEntity = userDao.getUserByUuid(userUuid);
        if (userEntity == null) {
            throw new UserNotFoundException("USR-001", "User with entered uuid does not exist");
        }

        /**
         * should be ideally now.isAfter(userAuthEntity.getExpiresAt()) || now.isAfter(userAuthEntity.getLogoutAt()
         */
        if (userAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get user details");
        }
        return userEntity;
    }
}
