import ui
import menus
from tests import *

test_all()
while True:
    choice = input('Type 1 for a console-based user interface, otherwise type 2 for a menu-based user interface: ')
    if choice == '1':
        ui.start()
        break
    elif choice == '2':
        menus.start()
        break
    else:
        print('Invalid choice')
