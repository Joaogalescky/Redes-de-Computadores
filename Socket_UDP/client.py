import socket


def client_program():
    # host
    udp_ip = socket.gethostname()  # as both code is running on same pc
    udp_port = 5005  # socket server port number

    server_address = (udp_ip, udp_port)

    print(f"UDP target IP: {udp_ip}")
    print(f"UDP target port: {udp_port}")

    # Create a UDP socket
    client_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)   # instantiate

    message = input(" -> ")  # take input

    while message.lower().strip() != 'bye':
        client_socket.sendto(message.encode(), server_address)  # send message

        data, server_address_reply = client_socket.recvfrom(1024)  # receive response
        data_decoded = data.decode()

        print('Received from server:' + data_decoded)  # show in terminal

        message = input(" -> ")  # again take input

    client_socket.sendto('bye'.encode(), server_address)

    client_socket.close()  # close the connection

if __name__ == '__main__':
    client_program()