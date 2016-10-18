'''
Author: Reese Wells
Copyright 2016
'''

from tkinter import Tk
from tkinter.filedialog import askopenfilename


class VoteCalc:

    def __init__(self):
        Tk().withdraw()
        self.voterdatafile = askopenfilename()
        self.voterverifyfile = askopenfilename()
        self.currentCounty = ""
        self.newCounty = County("")
        self.counties = []

    def verifyrecords(self):
        #todo implement verification
        return True

    def calcVotes(self):
        f = open(self.voterdatafile,'r')
        for line in f:
            data = [x.strip() for x in line.split(',')]
            if self.currentCounty != data[0]:
                self.currentCounty = data[0]
                self.newCounty = County(data[0])
                self.counties.append(self.newCounty)
            newDistrict = District(data[1], data[2], data[3], data[4])
            self.newCounty.addDistrict(newDistrict)  

class District:

    def __init__(self, name, repubVotes, demVotes, indVotes):
        self.name = name
        self.repub = int(repubVotes)
        self.dem = int(demVotes)
        self.ind = int(indVotes)
        self.totalVotes = self.repub + self.dem + self.ind

    def getDemVotes(self):
        return self.dem

    def getRepubVotes(self):
        return self.repub

    def getIndVotes(self):
        return self.ind

    def getTotal(self):
        return self.totalVotes()

    def getDemPercent(self):
        return self.dem/self.totalVotes

    def getRepPercent(self):
        return self.repub/self.totalVotes

    def getIndPercent(self):
        return self.ind/self.totalVotes

class County:

    def __init__(self, name):
        self.name = name
        self.districts = []
        self.dem = 0
        self.repub = 0
        self.ind = 0
        self.total = 0

    def getData(self):
        self.calcDemVotes()
        self.calcRepubVotes()
        self.calcIndVotes()
        self.calcTotalVotes()

    def addDistrict(self, district):
        self.districts.append(district)

    def calcDemVotes(self):
        for i in range (0,len(self.districts)):
            self.dem = self.dem + self.districts[i].getDemVotes()

    def calcRepubVotes(self):
        for i in range (0,len(self.districts)):
            self.repub = self.repub + self.districts[i].getRepubVotes()

    def calcIndVotes(self):
        for i in range (0,len(self.districts)):
            self.ind = self.ind + self.districts[i].getIndVotes()

    def calcTotalVotes(self):
        self.total = self.dem + self.repub + self.ind

    def demPercent(self):
        return 
