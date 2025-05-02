package com.deltacodex.ee.webapp.ejb;

import com.deltacodex.ee.webapp.remote.UserDetails;
import jakarta.ejb.Stateful;
//import jakarta.ejb.Stateless;

//@Stateless
@Stateful
public class UserDetailsBean implements UserDetails {
    @Override
    public String getUsername() {
        return "Finn Wolfhard";
    }

    @Override
    public String getEmail() {
        return "finn.2002@yahoo.com";
    }

    @Override
    public String getContact() {
        return "0753441289";
    }
}
