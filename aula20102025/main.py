# Calcular o endereço broadcast a partir de um IP e máscara de sub-rede

def calcular_broadcast(ip_str, mascara_str):
    """
    Args:
        ip_str (str): Endereço IP em formato string (ex: '192.168.1.100').
        mascara_str (str): Máscara de sub-rede em formato string (ex: '255.255.255.0').
    Returns:
        str: Endereço de broadcast em formato string, ou None se os IPs forem inválidos.
    """
    try:
        # Divide os endereços IP e máscara em octetos e converte para inteiros
        ip_octetos = [int(octeto) for octeto in ip_str.split('.')] #192.168.248.119 --> [192, 168, 248, 119]
        mascara_octetos = [int(octeto) for octeto in mascara_str.split('.')] #255.255.255.0 --> [255, 255, 255, 0]
        
        # Inverte a máscara para obter a máscara de wildcard
        mascara_inversa_octetos = [255 - octeto for octeto in mascara_octetos] #255 = 2⁸ - 1
        # 0 para rede
        # 1 para host
        
        # Realiza a operação OR bit a bit (ip | wildcard)
        broadcast_octetos = [ip_octetos[i] | mascara_inversa_octetos[i] for i in range(4)]
        
        # Converte a lista de octetos de volta para uma string de IP
        broadcast_str = '.'.join(str(octeto) for octeto in broadcast_octetos)
        
        return broadcast_str
        
    except (ValueError, IndexError):
        return None
    
def saida():
    ip = input("Digite o IP: ")
    mascara = input("Digite a Mascara: ")
    broadcast = calcular_broadcast(ip, mascara)
    if broadcast:
        print(f"IP: {ip}")
        print(f"Máscara: {mascara}")
        print(f"Endereço de broadcast: {broadcast}")
    else:
        print("Entrada inválida.")

saida()

# Exemplo de uso
# ip = '192.168.248.129'
# mascara = '255.255.255.0'
# ip2 = '10.0.32.10'
# mascara2 = '255.255.240.0'