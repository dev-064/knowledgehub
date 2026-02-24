package com.divyanshu.knowledgehub.application.port.out;

import com.divyanshu.knowledgehub.domain.model.User;

public interface UserRepository {
    User save(User user);
}
