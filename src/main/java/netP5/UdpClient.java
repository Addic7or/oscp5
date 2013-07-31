package netP5;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Collection;

public class UdpClient implements Sender {

	private final InetSocketAddress socket;

	private DatagramChannel channel;

	@SuppressWarnings( "unused" )
	private UdpClient( ) {
		socket = null;
	}

	public UdpClient( String theHost , int thePort ) {

		socket = new InetSocketAddress( theHost , thePort );

		try {

			channel = DatagramChannel.open( );

			channel.connect( socket );

		} catch ( IOException e ) {

			e.printStackTrace( );

		}

		/* TODO initialize buffer as well? */

	}

	public boolean send( byte[] theContent ) {
		try {

			ByteBuffer buffer = ByteBuffer.allocate( theContent.length );

			buffer.clear( );

			buffer.put( theContent );

			buffer.flip( );

			channel.send( buffer , socket );

			return true;

		} catch ( Exception e ) {
			System.err.println( "Could not send datagram " + e );
		}
		return false;
	}

	public boolean send( byte[] theContent , Collection< InetSocketAddress > theAddress ) {

		InetSocketAddress[] o = new InetSocketAddress[ theAddress.size( ) ];

		return send( theContent , theAddress.toArray( o ) );
	}

	public boolean send( byte[] theContent , String theHost , int thePort ) {

		return send( theContent , new InetSocketAddress( theHost , thePort ) );

	}

	public boolean send( byte[] theContent , SocketAddress ... theAddress ) {
		try {

			ByteBuffer buffer = ByteBuffer.allocate( theContent.length );

			buffer.clear( );

			buffer.put( theContent );

			buffer.flip( );

			DatagramChannel channel = DatagramChannel.open( );

			for ( SocketAddress addr : theAddress ) {
				channel.send( buffer , addr );
			}

			return true;

		} catch ( Exception e ) {
			System.err.println( "Could not send datagram " + e );
		}
		return false;
	}

	public static void main( String args[] ) {

		UdpClient udp = new UdpClient( "127.0.0.1" , 10000 );

		for ( int i = 0 ; i < 10 ; i++ ) {

			// UdpClient.send( ( "hello" + i ).getBytes( ) , "127.0.0.1" , 10000 );

			udp.send( ( "hello" + i ).getBytes( ) );

			try {
				Thread.sleep( 100 );
			} catch ( InterruptedException e ) {
				e.printStackTrace( );
			}
		}
	}
}
