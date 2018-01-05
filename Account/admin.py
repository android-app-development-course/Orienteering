# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.contrib import admin

# Register your models here.
from .models import *
# Register your models here.
admin.site.register(Account)
admin.site.register(Map)
admin.site.register(Match)
admin.site.register(Team)
admin.site.register(MatchOfPerson)
admin.site.register(Question)
