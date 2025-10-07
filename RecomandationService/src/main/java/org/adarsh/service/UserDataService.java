package org.adarsh.service;

import org.adarsh.entities.UserData;
import org.adarsh.model.UserDataModel;
import org.adarsh.repository.UserDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDataService {

    @Autowired
    private UserDataRepository userDataRepository;

    public UserData createOrUpdate( UserDataModel userDataModel ){
        UserData userData = convertToUserData( userDataModel );
        return userDataRepository.save(userData);
    }

    private UserData convertToUserData(UserDataModel userDataModel) {
        return UserData.builder()
                .email(userDataModel.getEmail())
                .preferences(userDataModel.getPreferences())
                .build();
    }
}
