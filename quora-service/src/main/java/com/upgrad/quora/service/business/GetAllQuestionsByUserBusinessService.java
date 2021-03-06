package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GetAllQuestionsByUserBusinessService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private QuestionDao questionDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public void verifyAuthTokenAndUuid(final String userUuid, final String authorizationToken) throws
            UserNotFoundException, AuthorizationFailedException {
        UserAuthEntity UserAuthEntity = userDao.getUserAuthToken(authorizationToken);
        if (userDao.getUserByUuid(userUuid) == null) {
            throw new UserNotFoundException("USR-001", "User with entered uuid whose question details are to be seen does not exist");
        } else if (UserAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        } else if (UserAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get all questions posted by a specific user");
        }
    }

    public List<QuestionEntity> getAllQuestionsByUserId(final long userId) {
        return questionDao.getAllQuestionsByUserId(userId);
    }

    public UserAuthEntity getUserAuthTokenByUuid(final String userUuid) {
        return userDao.getUserAuthTokenByUuid(userUuid);
    }
}