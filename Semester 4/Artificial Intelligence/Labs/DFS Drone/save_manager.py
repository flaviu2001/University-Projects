import pickle


def save_environment(environment, file_name):
    with open(file_name, 'wb') as f:
        pickle.dump(environment, f)
        f.close()


def load_environment(environment, file_name):
    with open(file_name, "rb") as f:
        dummy = pickle.load(f)
        environment.set_to(dummy)
        f.close()
