package com.tsdi.mcs;

import com.jphotonics.protocol.buffer.JPBufferFactory;
import com.jphotonics.protocol.transport.client.JPClientTransport;
import com.jphotonics.protocol.transport.client.JPClientTransportFactory;

import android.app.Application;

public class MCSApplication extends Application {
	private JPClientTransport client;

	public JPClientTransport getClient() {
		return client;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		client = JPClientTransportFactory.CreateTransport(JPClientTransportFactory.ZipSocketClient, JPBufferFactory.BinaryBuffer);
	}
}
