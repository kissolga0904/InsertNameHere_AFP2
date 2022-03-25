from kivy.app import App
from kivy.lang import Builder
from kivy.uix.boxlayout import BoxLayout
from kivy.uix.gridlayout import GridLayout
from kivy.uix.label import Label
from kivy.uix.popup import Popup
from kivy.uix.screenmanager import ScreenManager, Screen
from kivy.uix.slider import Slider
from kivymd.app import MDApp
from kivy.garden.matplotlib.backend_kivyagg import FigureCanvasKivyAgg
import matplotlib.pyplot as plt
import json
from kivy.core.window import Window

class Manager(ScreenManager):
    pass

class MenuScreen(Screen):
    def go_file_screen(self):
        self.manager.current = "choose_file_screen"

class ChooseFileScreen(Screen):
    def test_button(self, selection):
        self.ids.load_button.disabled = not self.can_load(selection)

    @staticmethod
    def can_load(selection):
        if len(selection) == 1 and selection[0].endswith(".json"):
            with open(selection[0]) as f:
                json_dict = json.load(f)
                if all(key in json_dict for key in ("accelerometer", "accelerometer_timestamps",
                                                    "gyroscope", "gyroscope_timestamps",
                                                    "magnetometer", "magnetometer_timestamps")):
                    return True
        return False

    def load(self, selection):
        if self.can_load(selection):
            self.manager.get_screen("choose_graph_screen").load(selection[0])
            self.manager.current = "choose_graph_screen"

class ChooseGraphScreen(Screen):
    def load(self, file):
        pass

class MainApp(MDApp):
    def build(self):
        Builder.load_file("insertnamehere.kv")
        self.theme_cls.theme_style = "Dark"
        self.theme_cls.primary_palette = "BlueGray"
        return Manager()

MainApp().run()


