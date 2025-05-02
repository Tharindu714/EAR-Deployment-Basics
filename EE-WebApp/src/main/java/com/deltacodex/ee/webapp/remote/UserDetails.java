package com.deltacodex.ee.webapp.remote;

import jakarta.ejb.Remote;

@Remote
public interface UserDetails {
    String getUsername();
    String getEmail();
    String getContact();
}
