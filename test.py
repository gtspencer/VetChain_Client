from flask import Flask
from flask_restful import Resource, Api, reqparse
from flask_mysqldb import MySQL
from flask import jsonify
from flask_cors import CORS, cross_origin
from flask import request

app = Flask(__name__)
api = Api(app)
CORS(app)

#Config MySQL and init app to work with MySQL
app.config["MYSQL_HOST"] = "localhost"
app.config["MYSQL_USER"] = "root"
app.config["MYSQL_PASSWORD"]=""
app.config["MYSQL_DB"]="kidush"
mysql = MySQL(app)

class connectionTest(Resource):
    def get(self):
    	try:
    		cursor = mysql.connection.cursor()
    		return true
    	except Exception as e:
    		return {"error":str(e)}
class videoCallList(Resource):
    def get(self):
    	try:
    		cursor = mysql.connection.cursor()
    		cursor.callproc('spGetVideoCallList')
    		data=cursor.fetchall()
    		jsondata = []
    		for row in data:
    			jsondata.append({'id':str(row[0]),
    				               'studentID':str(row[1]),
    				               'educatorID':str(row[2]),
    				               'subject':row[3],
    				               'startTime':row[4],
    				               'endTime':row[5],
    				               'videolink':row[6]})    		
    		return jsonify(jsondata)
    	except Exception as e:
    		return{"error": str(e)}

class insertVideoCall(Resource):
    def put(self):
    	try:
    		parser = reqparse.RequestParser()
    		parser.add_argument("studentID", type=int, help= "studentID required to restrive video call list")
    		parser.add_argument("educatorID", type=int, help= "educatorID required to restrive video call list")
    		parser.add_argument("subject", type=str, help= "subjectID required to restrive video call list")
    		parser.add_argument("startTime",type=str, help="startTime can't be empty")
    		parser.add_argument("endTime",type=str,help="endTime can't be empty")
    		parser.add_argument("videolink",type=str,help="videolink can't be empty")
    		args = parser.parse_args()
    		_studentID = args["studentID"]
    		_educatorID=args["educatorID"]
    		_subject = args["subject"]
    		_startTime = args["startTime"]
    		_endTime = args["endTime"]
    		_videolink = args["videolink"]
    		cursor = mysql.connection.cursor()#get db cursor to send query to db
    		cursor.callproc('spInsertVideoCall',(_studentID,_educatorID,_subject,_startTime,_endTime,_videolink))
    		mysql.connection.commit()#required to after making change to the db
    		return "Insert success"
    	except Exception as e:
    		return {"error":str(e)}
    def post(self):
    	try:
    		parser = reqparse.RequestParser()
    		parser.add_argument("studentID", type=int, help= "studentID required to restrive video call list")
    		parser.add_argument("educatorID", type=int, help= "educatorID required to restrive video call list")
    		parser.add_argument("subject", type=str, help= "subjectID required to restrive video call list")
    		parser.add_argument("startTime",type=str, help="startTime can't be empty")
    		parser.add_argument("endTime",type=str,help="endTime can't be empty")
    		parser.add_argument("videolink",type=str,help="videolink can't be empty")
    		args = parser.parse_args()
    		_studentID = args["studentID"]
    		_educatorID=args["educatorID"]
    		_subject = args["subject"]
    		_startTime = args["startTime"]
    		_endTime = args["endTime"]
    		_videolink = args["videolink"]
    		cursor = mysql.connection.cursor()#get db cursor to send query to db
    		cursor.callproc('spInsertVideoCall',(_studentID,_educatorID,_subject,_startTime,_endTime,_videolink))
    		mysql.connection.commit()#required to after making change to the db
    		return "Insert success"
    	except Exception as e:
    		return {"error":str(e)}

api.add_resource(videoCallList, '/video_call_list',methods=['GET'])
api.add_resource(insertVideoCall, '/video_call_list/insert',methods=['POST','PUT'])
if __name__ == '__main__':
    app.run(debug=True)
