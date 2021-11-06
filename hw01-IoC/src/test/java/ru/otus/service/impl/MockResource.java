package ru.otus.service.impl;

import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

@SuppressWarnings({"all"})
public class MockResource implements Resource {
    private final InputStream inputStream;

    public MockResource(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public boolean exists() {
        throw new UnsupportedOperationException();
    }

    @Override
    public URL getURL() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public URI getURI() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public File getFile() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long contentLength() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long lastModified() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Resource createRelative(String s) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getFilename() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getDescription() {
        throw new UnsupportedOperationException();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return inputStream;
    }
}
