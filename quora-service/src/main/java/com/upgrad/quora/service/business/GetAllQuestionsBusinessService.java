package com.upgrad.quora.service.business;

import com.upgrad.quora.service.common.Identifier;
import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GetAllQuestionsBusinessService implements Identifier {

    @Autowired
    private QuestionDao questionDao;
    @Autowired
    private UserDao userDao;

    public List<QuestionEntity> getAllQuestions() throws AuthorizationFailedException {
        return questionDao.getAllQuestions();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthEntity verifyAuthToken(final String accessToken) throws AuthorizationFailedException {
        UserAuthEntity UserAuthEntity = userDao.getUserAuthToken(accessToken);
        if (UserAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        } else if (UserAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get all questions");
        }
        return UserAuthEntity;

    }
}