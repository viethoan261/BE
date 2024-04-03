package hoan.com.springboot.security.services;

import hoan.com.springboot.common.UserAuthority;

public interface AuthorityService {

    UserAuthority getUserAuthority(String userId);
}
