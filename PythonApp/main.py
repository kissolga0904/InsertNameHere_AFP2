from kivy.lang import Builder
from kivy.uix.boxlayout import BoxLayout
from kivy.uix.popup import Popup
from kivy.uix.screenmanager import ScreenManager, Screen
from kivymd.app import MDApp
from kivy.garden.matplotlib.backend_kivyagg import FigureCanvasKivyAgg
import matplotlib.pyplot as plt
import json

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
    def __init__(self,**kw):
        super().__init__(**kw)
        self.dict = None

    def load(self, file):
        with open(file) as f:
            self.dict = json.load(f)

    def show_graph(self,graph_type:str):
        if graph_type == "accelerometer" or graph_type == "gyroscope" or graph_type == "magnetometer":
            time_array = self.dict[graph_type + "_timestamps"]
            time_zero = time_array[0]
            time = [t - time_zero for t in time_array]
            values = self.dict[graph_type]
            x = [float(s.split(";")[0]) for s in values]
            y = [float(s.split(";")[1]) for s in values]
            z = [float(s.split(";")[2]) for s in values]
            self.manager.get_screen("graph_screen").load_xyz(graph_type,time,x,y,z)
        self.manager.current = "graph_screen"

class GraphScreen(Screen):
    def __init__(self, **kw):
        super().__init__(**kw)
        self.graph_type = ""

    def load_xyz(self, graph_type:str, time, x, y, z):
        self.graph_type = graph_type
        plt.close("all")
        plt.figure(0)
        plt.cla()
        box = self.ids.graph
        box.clear_widgets()
        plt.plot(time, x, label="x")
        plt.plot(time, y, label="y")
        plt.plot(time, z, label="z")
        plt.legend()
        plt.grid()
        plt.title(graph_type.replace("_"," ").title())
        plt.xlabel("Time")
        switcher = {
            "accelerometer": "Acceleration (m/s²)",
            "gyroscope": "Rotation rate (rad/s)",
            "magnetometer": "Magnetic field (μT)"
        }
        plt.ylabel(switcher.get(graph_type))
        box.add_widget(FigureCanvasKivyAgg(plt.gcf()))

    def export(self):
        plt.savefig(self.graph_type)
        popup = Popup(
            title="Successfully export " + self.graph_type,
            content = BoxLayout(),
            size_hint = (.4,.2)
        )
        popup.open()


class MainApp(MDApp):
    def build(self):
        Builder.load_file("insertnamehere.kv")
        self.theme_cls.theme_style = "Dark"
        self.theme_cls.primary_palette = "BlueGray"
        return Manager()

MainApp().run()


