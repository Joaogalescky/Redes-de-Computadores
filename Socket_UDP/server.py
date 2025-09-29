import socket


def server_program():
    # get the hostname
    udp_ip = socket.gethostname()
    udp_port = 5005  # initiate port no above 1024

    server_address = (udp_ip, udp_port)

    # Create a UDP socket
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)  # get instance
    # look closely. The bind() function takes tuple as argument
    server_socket.bind(server_address)  # bind host address and port together

    print(f"Listening for UDP packets on {udp_ip}:{udp_port}")
    while True:
        # receive data stream. it won't accept data packet greater than 1024 bytes
        data, address = server_socket.recvfrom(1024)
        if not data:
            continue

        data_decoded = data.decode()

        if data_decoded.lower().strip() == 'bye':
            print(f"Client at {address} sent 'bye'.")
            server_socket.sendto("Goodbye!".encode(), address)
            break

        print(f"from connected user: {data_decoded} from {address}")

        message = input(' -> ')
        server_socket.sendto(message.encode(), address)  # send data to the client

    server_socket.close()  # close the connection


if __name__ == '__main__':
    server_program()