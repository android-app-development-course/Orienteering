"""hello URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/1.11/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  url(r'^$', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  url(r'^$', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.conf.urls import url, include
    2. Add a URL to urlpatterns:  url(r'^blog/', include('blog.urls'))
"""
from django.conf.urls import url
from django.contrib import admin
from testtime import views as testtimes_views
from Account import views as AccountViews
urlpatterns = [
    url(r'^admin/', admin.site.urls),
	url(r'^test/addtimestamp/$',testtimes_views.test1,name='test1'),
	 url(r'^test/firstTime/$',testtimes_views.test2,name='test2'),
 url(r'^test/lastTime/$',testtimes_views.test3,name='test3'),
	url(r'^test/getMatchPoints/$',testtimes_views.getMatchPoints,name='getMatchPoints'),
 url(r'^test/test1/$',testtimes_views.test5),

url(r'^ispersoninteam/$',AccountViews.isPersonInTeam),
url(r'^getmatchpassword/$',AccountViews.getMatchPassword),
url(r'^getpersondaymile/$',AccountViews.getPersonDayMile),
url(r'^uploadcid/$',AccountViews.uploadCID),
url(r'^getpersonoverview/$',AccountViews.getPersonOverview),
url(r'^uploadhead/$',AccountViews.uploadHead),
url(r'^createaccount/$',AccountViews.createAccount),
    url(r'^changepassword/$',AccountViews.changePassword),
    url(r'^changeinformation/$',AccountViews.changeInfo),
    url(r'^getinformation/$',AccountViews.getInfo),
    url(r'^getid/$',AccountViews.getID),
    url(r'^getmatchofperson/$',AccountViews.getMatchOfPerson),
    url(r'^getmaplist/$',AccountViews.getMapList),
    url(r'^getmappoints/$',AccountViews.getMapPoints),
    url(r'^getmatchlist/$',AccountViews.getMatchList),
    url(r'^jointeam/$',AccountViews.joinTeam),
    url(r'^getteamlist/$',AccountViews.getTeamList),
    url(r'^uploadpoint/$',AccountViews.uploadPoint),
    url(r'^gethavepointed/$',AccountViews.getHavePoint),
   url(r'^uploadrecodinglength/$',AccountViews.uploadRecodingLength),
    url(r'^getrecodingpoint/$',AccountViews.getRecodingPoint),
    url(r'^uploadrecodingpoint/$',AccountViews.uploadRecodingPoint),
    url(r'^getpersonrank/$',AccountViews.getPersonRank),
    url(r'^getmatchrank/$',AccountViews.getMatchRank),
    url(r'^gettypeinfo/$',AccountViews.getTypeInfo),
    url(r'^getnameidtimeofmatchperson/$',AccountViews.PersonOfMatch),
    url(r'^getquestion/$',AccountViews.getQuestion),
    url(r'^createmap/$',AccountViews.createMap),
    url(r'^createteam/$',AccountViews.createTeam),
    url(r'^creatematch/$',AccountViews.createMatch),
    url(r'^getteamtime/$',AccountViews.getTeamTime),
]
