package com.cermak.realboss.exception;

import java.io.IOException;

public class FileStorageException extends RuntimeException{
    public FileStorageException(String message, IOException e)  {
        super(message);
    }
}
