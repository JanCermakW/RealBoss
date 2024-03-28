package com.cermak.realboss.service;

import com.cermak.realboss.model.MailStructure;

public interface MailService {
    public void sendMail(String mail, MailStructure mailStructure);
}
