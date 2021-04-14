from flask import Flask, render_template
from flask_cors import CORS

app = Flask(__name__)
cors = CORS(app)



@app.route("/")
def home():
  return render_template('home.html')



if __name__ == '__main__':
  
    # run() method of Flask class runs the application 
    # on the local development server.
    app.run()