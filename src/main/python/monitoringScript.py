import psutil
import socket
import time
import json
import threading
import sys
import os
import tkinter as tk
from tkinter import simpledialog, messagebox
from pystray import Icon, MenuItem, Menu
from PIL import Image

def get_icon_path():
    if getattr(sys, 'frozen', False):  #check if running as a PyInstaller executable
        return os.path.join(sys._MEIPASS, "a.ico")  #extracted temp folder
    return "a.ico"

ICON_PATH = get_icon_path()
image = Image.open(ICON_PATH)

def get_ip_address():
    root = tk.Tk()
    root.withdraw()  #hide root window
    while True:
        ip = simpledialog.askstring("Enter IP", "Enter the destination IP address:")
        if not ip:
            messagebox.showerror("Error", "No IP address entered. Please try again.")
            continue
        try:
            socket.inet_aton(ip)  #checks for valid IPv4 address
            return ip  #valid IP entered - return it
        except socket.error:
            messagebox.showerror("Error", "Invalid IP address format. Please enter a valid IPv4 address.")

#prompt user for server IP address using GUI
SERVER_IP = get_ip_address()

#server port
SERVER_PORT = 55440

def get_metrics():
    cpu_usage = psutil.cpu_percent(interval=1)
    ram = psutil.virtual_memory()
    ram_usage = ram.used / (1024 ** 3)
    free_ram = ram.available / (1024 ** 3)
    return cpu_usage, ram_usage, free_ram

def send_metrics():
    sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    try:
        while True:
            cpu_usage, ram_usage, free_ram = get_metrics()
            message = json.dumps({
                "timestamp": time.time(),
                "cpu_usage": cpu_usage,
                "ram_usage": ram_usage,
                "free_ram": free_ram
            })
            sock.sendto(message.encode(), (SERVER_IP, SERVER_PORT))
            time.sleep(60)
    finally:
        sock.close()

def exit_app(icon):
    icon.stop()
    exit(0)

def run_tray():
    menu = Menu(MenuItem("Exit", exit_app))
    tray_icon = Icon("MetricsSender", image, menu=menu)
    tray_icon.run()

if __name__ == "__main__":
    threading.Thread(target=send_metrics, daemon=True).start()
    try:
        import ctypes
        ctypes.windll.kernel32.FreeConsole()
    except Exception:
        pass
    run_tray()
