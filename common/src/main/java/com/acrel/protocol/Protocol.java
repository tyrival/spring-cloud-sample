package com.acrel.protocol;

import lombok.Data;

@Data
public class Protocol {

    private ProtocolEncoder encoder;

    private ProtocolDecoder decoder;

    public Protocol(ProtocolEncoder encoder, ProtocolDecoder decoder) {
        this.decoder = decoder;
        this.encoder = encoder;
    }
}
