'''
Author: Reese Wells
Copyright 2016
'''

from tkinter import Tk
from tkinter.filedialog import askopenfilename
import numpy as np
import matplotlib.pyplot as plt

class State:

    def __init__(self):
        Tk().withdraw()
        self.voterdatafile = askopenfilename()
        #self.voterverifyfile = askopenfilename()
        self.currentCounty = ""
        self.newCounty = County("")
        self.counties = []

    def verifyrecords(self):
        #todo implement verification
        return True

    def getCountiesAndDistricts(self):
        """
        1. Takes the voter data from the file specified above and reads the info
        2. Creates counties and saves their districts
        """
        f = open(self.voterdatafile,'r')
        
        for line in f:
            data = [x.strip() for x in line.split(',')] #separate by comma
            if self.currentCounty != data[0]: #check if found new county
                self.currentCounty = data[0] #name county
                self.newCounty = County(data[0]) #save county
                self.counties.append(self.newCounty) #add county to lsit
            newDistrict = District(data[1], data[2], data[3], data[4]) #create district
            self.newCounty.addDistrict(newDistrict)  #add district to county

        for i in range(0,len(self.counties)): #initializes all the counties
            self.counties[i].getData()

    def selectCounty(self, name):
        for i in range(0, len(self.counties)):
            if name == self.counties[i].getName():
                return self.counties[i]

class County:

    def __init__(self, name):
        self.name = name
        self.districts = []
        self.dem = 0
        self.repub = 0
        self.ind = 0
        self.total = 0

    def selectDistrict(self, name):
        for i in range(0, len(self.districts)):
            if name == self.districts[i].getName():
                return self.districts[i]

    def getName(self):
        return self.name

    def getData(self):
        """
        Run this when all the districts have been added to a county
        1. calculates the total number of votes in a county
        """
        self.calcDemVotes()
        self.calcRepubVotes()
        self.calcIndVotes()
        self.calcTotalVotes()

    def addDistrict(self, district):
        """
        @param
                district, a district object: adds a district to a county
        """
        self.districts.append(district)

    def calcDemVotes(self):
        """
        calculates the total number of democratic votes in a county
        """
        for i in range (0,len(self.districts)):
            self.dem = self.dem + self.districts[i].getDemVotes()

    def getDemVotes(self):
        """
        @return
                int, the number of democratic votes in a county
        """
        return self.dem

    def calcRepubVotes(self):
        """
        calculates the total number of republican votes in a county
        """
        for i in range (0,len(self.districts)):
            self.repub = self.repub + self.districts[i].getRepubVotes()

    def getRepubVotes(self):
        """
        @return
                int, the number of republican votes in a county
        """
        return self.repub

    def calcIndVotes(self):
        """
        calculates the total number of independent votes in a county
        """
        for i in range (0,len(self.districts)):
            self.ind = self.ind + self.districts[i].getIndVotes()

    def getIndVotes(self):
        """
        @return
                int, the number of independent votes in a county
        """
        return self.ind

    def calcTotalVotes(self):
        """
        calculates the total number of votes in a county
        """
        self.total = self.dem + self.repub + self.ind

    def getDemPercent(self):
        """
        @return
                int, the percentage of democratic votes in a county
        """
        return self.dem/self.total

    def getRepubPercent(self):
        """
        @return
                int, the percentage of republican votes in a county
        """
        return self.repub/self.total

    def getIndPercent(self):
        """
        @return
                the percentage of independent votes in a county
        """
        return self.ind/self.total

class District:

    def __init__(self, name, repubVotes, demVotes, indVotes):
        self.name = name
        self.repub = int(repubVotes)
        self.dem = int(demVotes)
        self.ind = int(indVotes)
        self.totalVotes = self.repub + self.dem + self.ind

    def getName(self):
        return self.name

    def getDemVotes(self):
        """
        @return
                int, the number of democratic votes in a district
        """
        return self.dem

    def getRepubVotes(self):
        """
        @return
                int, the number of republican votes in a district
        """
        return self.repub

    def getIndVotes(self):
        """
        @return
                int, the number of independent votes in a district
        """
        return self.ind

    def getTotal(self):
        """
        @return
                int, the total number of votes in a district
        """
        return self.totalVotes()

    def getDemPercent(self):
        """
        @return
                the percentage of democratic voters in a district
        """
        return self.dem/self.totalVotes

    def getRepPercent(self):
        """
        @return
                the percentage of republican voters in a district
        """
        return self.repub/self.totalVotes

    def getIndPercent(self):
        """
        @return
                the percentage of independent voters in a district
        """
        return self.ind/self.totalVotes

class Grapher:

    def barGraphCounty(county):
        N = len(county.districts)
        
        districtDemVotes = []
        districtRepubVotes = []
        districtIndVotes = []
        
        for i in range(0, len(county.districts)):
            districtDemVotes.append(county.districts[i].getDemVotes())
            
        for i in range(0, len(county.districts)):
            districtRepubVotes.append(county.districts[i].getRepubVotes())
            
        for i in range(0, len(county.districts)):
            districtIndVotes.append(county.districts[i].getIndVotes())

        ind = np.arange(N)  # the x locations for the groups
        width = 0.35       # the width of the bars

        fig, ax = plt.subplots()
        rects1 = ax.bar(ind, districtDemVotes, width, color='b', yerr = 0)
        rects2 = ax.bar(ind + width, districtRepubVotes, width, color='r', yerr = 0)
        rects3 = ax.bar(ind + width*2, districtIndVotes, width, color='g', yerr = 0)

        # add some text for labels, title and axes ticks
        ax.set_ylabel('Number of Votes')
        ax.set_title('Votes Divided by District')
        ax.set_xticks(ind + width)

        districtNames = []
        for i in range(0, len(county.districts)):
            districtNames.append(county.districts[i].getName())
        
        ax.set_xticklabels(districtNames)

        ax.legend((rects1[0], rects2[0], rects3[0]), ('Democratic', 'Republican', 'Independent'))


        def autolabel(rects):
            # attach some text labels
            for rect in rects:
                height = rect.get_height()
                ax.text(rect.get_x() + rect.get_width()/2., 1.05*height,
                        '%d' % int(height),
                        ha='center', va='bottom')

        autolabel(rects1)
        autolabel(rects2)
        autolabel(rects3)

        plt.show()
        

Ohio = State()
Ohio.getCountiesAndDistricts()
County1 = Ohio.selectCounty("Adams")
