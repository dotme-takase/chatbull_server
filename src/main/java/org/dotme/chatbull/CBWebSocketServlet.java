package org.dotme.chatbull;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CBWebSocketServlet extends WebSocketServlet {

	private static final long serialVersionUID = -8788861893056284284L;
	Logger log = LoggerFactory.getLogger(CBWebSocketServlet.class);

	// string には WebSocket のサブプロトコルが入る
	@Override
	public WebSocket doWebSocketConnect(HttpServletRequest hsr, String string) {
		// WebSocket インタフェースを実装したクラスのインスタンスを返す
		return new WebSocket.OnTextMessage() {

			protected Connection connection;

			// テキストのメッセージが届いたとき
			@Override
			public void onMessage(String data) {
				log.info("onMessage: " + data);
				// エコーする
				this.send(data);
			}

			// コネクションが開かれたとき
			@Override
			public void onOpen(Connection connection) {
				log.info("onOpen");
				this.setConnection(connection);
				this.send("Hello");
			}

			// コネクションが閉じられたとき
			@Override
			public void onClose(int closeCode, String message) {
				this.send("Bye");
				log.info("onClose");
			}

			public Connection getConnection() {
				return connection;
			}

			public void setConnection(Connection connection) {
				this.connection = connection;
			}

			protected void send(String message) {
				try {
					this.getConnection().sendMessage(message);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		};
	}
}