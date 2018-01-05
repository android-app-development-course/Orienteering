# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.shortcuts import render

from django.db import models
import time
from models import timestamp
import json,os
from django.http import HttpResponse
def test5(request):
    dir=os.getcwd()
    return HttpResponse(json.dumps({'dir':dir}))


def test1(request):
    timestr=time.strftime('%Y-%m-%d %H:%M:%S',time.localtime(time.time()))
    timestamp.objects.create(timestr=timestr)
    return HttpResponse(json.dumps({'time':timestr}))
def test2(request):
    times=timestamp.objects.all()
    FirstTimeStr=times[0].timestr
    LastTimeStr=times[len(times)-1].timestr
    return HttpResponse(json.dumps({'firstTime':FirstTimeStr}))
def test3(request):
    times=timestamp.objects.all()
    FirstTimeStr=times[0].timestr
    LastTimeStr=times[len(times)-1].timestr
    return HttpResponse(json.dumps({'lastTime':LastTimeStr}))
def getMatchPoints(request):
    id=int(request.GET['id'])
    type=int(request.GET['type'])
    points=[[23.138000, 23.138000,23.14000, 23.14000],[113.34968023003862,113.34668023003482,113.34668023003472, 113.34968023003472]]
    if type==0:
        return HttpResponse(json.dumps({'markerGroupID':id,
                                   'markerGroupNum':type,
                                   'markerGroupArray':points,
                                   'point':[]}))
    elif type==1:
        return HttpResponse(json.dumps({'markerGroupID': id,
                                        'markerGroupNum': type,
                                        'markerGroupArray': points,
                                        'point': [1,2,3,4]}))
# Create your views here.
