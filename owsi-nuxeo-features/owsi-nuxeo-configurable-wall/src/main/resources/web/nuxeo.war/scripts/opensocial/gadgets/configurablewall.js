(function() {

  /* constants */
  var constants = {
    likeOperationId: 'Services.Like',
    cancelLikeOperationId: 'Services.CancelLike',
    wallOperationId: 'OpenWide.GetConfigurableWallStream',

    miniMessageVerb: 'minimessage',

    filter: {
      all: "all",
      discussions: "discussions",
      events: "events"
    },

    noActivityTypeIcon: "icons/activity_empty.png"
  };
  /* end constants */

  /* templates */
  var templates = {};
  
  templates.activity =
      '<div class="activityContainerItem jsMainActivity" data-activityid="{{id}}" data-likescount="{{likeStatus.likesCount}}" data-userlikestatus="{{likeStatus.userLikeStatus}}">' +
        '<div class="container">' +
          '<div class="activityTypeContainer">' +
            '<span class="activityType"><img src="{{icon}}"></span>' +
          '</div>' +
          '<div class="activityContentContainer">' +
            '<div class="message">' +
              '<span class="avatar"><img src="{{actorAvatarURL}}" alt="{{displayActor}}" /></span>' +
              '<div class="event">{{{activityMessage}}}</div>' +
            '</div>' +
            '<div class="actions jsActions">' +
              '<span class="timestamp">{{publishedDate}}</span>' +
            '</div>' +
            '<div class="answers jsRepliesContainer">{{{repliesHtml}}}</div>' +
          '</div>' +
        '</div>' +
      '</div>';

  templates.miniMessage =
      '<div class="activityContainerItem jsMainActivity" data-activityid="{{id}}" data-likescount="{{likeStatus.likesCount}}" ' +
          'data-userlikestatus="{{likeStatus.userLikeStatus}}" data-allowdeletion="{{allowDeletion}}">' +
        '<div class="container">'+
          '<div class="activityTypeContainer">' +
            '<span class="activityType"><img src="{{icon}}"></span>' +
          '</div>' +
          '<div class="activityContentContainer">' +
            '<div class="messageHeader">' +
              '<span class="avatar"><img src="{{actorAvatarURL}}" alt="{{displayActor}}" /></span>' +
              '{{{displayActorLink}}}' +
            '</div>' +
            '<div class="message">{{{activityMessage}}}</div>' +
            '<div class="actions jsActions">' +
              '<span class="timestamp">{{{publishedDate}}}</span>' +
            '</div>' +
            '<div class="answers jsRepliesContainer">{{{repliesHtml}}}</div>' +
          '</div>' +
        '</div>' +
      '</div>';

  templates.reply =
      '<div class="activityContainerItem {{replyClass}}" data-replyid="{{id}}" data-likescount="{{likeStatus.likesCount}}" ' +
          'data-userlikestatus="{{likeStatus.userLikeStatus}}" data-allowdeletion="{{allowDeletion}}">' +
        '<div class="container">' +
          '<div class="message">' +
            '<span class="avatar"><img src="{{actorAvatarURL}}" alt="{{displayActor}}" /></span>' +
            '<div class="event">' +
              '{{{displayActorLink}}}' +
              '<div class="message">{{{message}}}</div>' +
            '</div>' +
          '</div>' +
          '<div class="actions jsReplyActions">' +
            '<span class="timestamp">{{{publishedDate}}}</span>' +
          '</div>' +
        '</div>' +
      '</div>';

  templates.deleteActivityAction =
      '<div class="actionItem jsDelete" data-activityid="{{activityId}}">' +
        '<img src="{{deleteImageURL}}" />' +
        '<a href="#">{{deleteMessage}}</a>' +
      '</div>';

  templates.deleteActivityReplyAction =
      '<div class="actionItem jsDelete" data-replyid="{{replyId}}">' +
        '<img src="{{deleteImageURL}}" />' +
        '<a href="#">{{deleteMessage}}</a>' +
      '</div>';

  templates.replyAction =
      '<div class="actionItem jsReply" data-activityid="{{activityId}}">' +
        '<img src="{{replyImageURL}}" />' +
        '<a href="#">{{replyMessage}}</a>' +
      '</div>';

  templates.likeAction =
      '<div class="actionItem jsLike">' +
        '<img class="likeIcon jsLikeIcon" src="{{likeImageURL}}" />' +
        '<span class="likesCount">{{likesCount}}</span>' +
      '</div>';

  templates.moreActivitiesBar =
    '<div class="moreActivitiesBar jsMoreActivitiesBar">{{moreActivitiesMessage}}</div>';

  templates.noMoreActivitiesBar =
      '<div class="moreActivitiesBar noMore">{{noMoreActivitiesMessage}}</div>';

  templates.moreRepliesBar =
      '<div class="moreActivitiesBar jsMoreRepliesBar">{{moreRepliesMessage}}</div>';

  templates.newActivitiesBar =
      '<div class="newActivitiesBar jsNewActivitiesBar">{{newActivitiesMessage}}</div>';
  /* end templates */

  var prefs = new gadgets.Prefs();

  var activityStreamName = prefs.getString("activityStreamName");
  var documentContextPath = gadgets.util.unescapeString(prefs.getString("nuxeoTargetContextPath"));

  var wallOperationParams = {
    language: prefs.getLang(),
    timeZone: prefs.getString("timeZone"),
    context: 'OpenWide:UserSocialWorkspacesStream', // TODO allow to use documentContextPath or a custom rule through a parameter
    activityStreamName: activityStreamName,
    activityLinkBuilder: prefs.getString("activityLinkBuilder")
  };
  var  miniMessageOperationParams = {
    language: prefs.getLang(),
    contextPath: documentContextPath
  };

  var currentActivities = [];
  var waitingActivities = [];

  var offset = 0;
  var waitingOffset = 0;

  var hasMoreActivities = true;

  var filter = constants.filter.all;

  function loadWallActivityStream() {
    var NXRequestParams = {
      operationId: constants.wallOperationId,
      operationParams: wallOperationParams,
      operationContext: {},
      operationCallback: function(response, params) {
        currentActivities = response.data.activities;
        offset = response.data.offset;
        displayWallActivities();
      }
    };

    doAutomationRequest(NXRequestParams);
  }

  function pollWallActivityStream() {
    var NXRequestParams= { operationId : constants.wallOperationId,
      operationParams: wallOperationParams,
      operationContext: {},
      operationCallback: function(response, params) {
        var newActivities = response.data.activities;
        if (newActivities.length > 0 && currentActivities[0].id !== newActivities[0].id) {
          // there is at least one new activity
          waitingActivities = newActivities;
          waitingOffset = response.data.offset;
          addNewActivitiesBarHtml();
          registerNewActivitiesBarHandler();
          gadgets.window.adjustHeight();
        }
      }
    };

    doAutomationRequest(NXRequestParams);
  }

  function displayWallActivities() {
    var htmlContent = '';

    if (currentActivities.length == 0) {
      htmlContent += '<div class="noStream">' + prefs.getMsg('label.no.activity') + '</div>';
    } else {
      for (var i = 0; i < currentActivities.length; i++) {
        var currentActivity = currentActivities[i];
        if (currentActivity.activityVerb == constants.miniMessageVerb && filter !== constants.filter.events) {
          htmlContent += buildActivityHtml(templates.miniMessage, currentActivity);
        } else if (currentActivity.activityVerb !== constants.miniMessageVerb && filter !== constants.filter.discussions) {
          htmlContent += buildActivityHtml(templates.activity, currentActivity);
        }
      }
    }
    $('#container').html(htmlContent);

    addLikeStatusHtml();
    addDeleteLinksHtml();
    addReplyLinksHtml();

    registerLikeStatusHandler();
    registerDeleteLinksHandler();
    registerReplyLinksHandler();
    registerNewActivityReplyHandler();
    registerMoreRepliesHandler();

    if (hasMoreActivities) {
      addMoreActivitiesBarHtml();
      registerMoreActivityBarHandler();
    } else {
      addNoMoreActivitiesTextHtml();
    }
    gadgets.window.adjustHeight();
  }

  function buildActivityHtml(template, activity) {
    var repliesHtml = '';
    if (activity.replies.length > 0) {
      if (activity.replies.length > 3) {
        var moreRepliesMessage = prefs.getMsg('label.view.all') + ' ' +
          activity.replies.length + ' ' + prefs.getMsg('label.activity.replies');
          repliesHtml += Mustache.render(templates.moreRepliesBar, {
          moreRepliesMessage: moreRepliesMessage
        });
      }
      for (var i = 0; i < activity.replies.length; i++) {
        var reply = activity.replies[i];
        reply.replyClass = i < activity.replies.length - 3 ? 'displayN' : '';
        repliesHtml += Mustache.render(templates.reply, reply);
      }
    }

    repliesHtml += Mustache.render(templates.newActivityReply, {
      activityId: activity.id,
      placeholderMessage: prefs.getMsg('label.placeholder.new.activity.reply'),
      writeLabel: prefs.getMsg('command.reply') });

    activity.repliesHtml = repliesHtml;
    var icon = activity.icon;
    if (icon != null && icon.length > 0) {
      if (icon.indexOf(NXGadgetContext.clientSideBaseUrl) < 0) {
        if (icon[0] == '/') {
          icon = icon.substring(1);
        }
        icon = NXGadgetContext.clientSideBaseUrl + icon;
      }
    } else {
      icon = NXGadgetContext.clientSideBaseUrl + constants.noActivityTypeIcon;
    }
    activity.icon = icon;
    return Mustache.render(template, activity);
  }

  function addDeleteLinksHtml() {
    // activities
    $('div[data-activityid][data-allowdeletion="true"]').each(function() {
      $(this).removeAttr('data-allowdeletion');
      var activityId = $(this).attr('data-activityid');
      var deleteImageURL = NXGadgetContext.clientSideBaseUrl + 'icons/delete.png'

      var actions = $(this).find('div.jsActions');
      var htmlContent = Mustache.render(templates.deleteActivityAction,
          { activityId: activityId, deleteImageURL: deleteImageURL,
            deleteMessage: prefs.getMsg('command.delete') });
      $(htmlContent).insertAfter(actions.find('.timestamp'));
    });

    // activity replies
    $('div[data-replyid][data-allowdeletion="true"]').each(function() {
      $(this).removeAttr('data-allowdeletion');
      var replyId = $(this).attr('data-replyid');
      var deleteImageURL = NXGadgetContext.clientSideBaseUrl + 'icons/delete.png'

      var actions = $(this).find('div.jsReplyActions');
      var htmlContent = Mustache.render(templates.deleteActivityReplyAction,
          { replyId: replyId, deleteImageURL: deleteImageURL,
            deleteMessage: prefs.getMsg('command.delete') });
      $(htmlContent).insertAfter(actions.find('.timestamp'));
    });
  }

  function addLikeStatusHtml() {
    $('div[data-activityid][data-likescount]').each(function() {
      var activityId = $(this).attr('data-activityid');
      var likesCount = $(this).attr('data-likescount');
      var userLikeStatus = $(this).attr('data-userlikestatus');
      var actions = $(this).find('div.jsActions');
      addActivityLikeStatusHtml(actions, activityId, likesCount, userLikeStatus);
    });

    $('div[data-replyid][data-likescount]').each(function() {
      var replyId = $(this).attr('data-replyid');
      var likesCount = $(this).attr('data-likescount');
      var userLikeStatus = $(this).attr('data-userlikestatus');
      var actions = $(this).find('div.jsReplyActions');
      addActivityLikeStatusHtml(actions, replyId, likesCount, userLikeStatus);
    });
  }

  function addActivityLikeStatusHtml(actions, activityId, likesCount, userLikeStatus) {
    actions.find('.jsLike').remove();

    var likeImageURL = userLikeStatus == 1
        ? NXGadgetContext.clientSideBaseUrl + 'icons/like_active.png'
        : NXGadgetContext.clientSideBaseUrl + 'icons/like_unactive.png';

    var htmlContent = Mustache.render(templates.likeAction,
        { likeImageURL: likeImageURL, likesCount: likesCount });

    var deleteAction = $(actions).find('.jsDelete');
    if (deleteAction.length > 0) {
      $(htmlContent).insertAfter(deleteAction);
    } else {
      $(htmlContent).insertAfter(actions.find('.timestamp'));
    }
  }

  function addReplyLinksHtml() {
    $('div[data-activityid]').each(function() {
      var activityId = $(this).attr('data-activityid');
      var replyImageURL = NXGadgetContext.clientSideBaseUrl + 'icons/reply.png'

      var actions = $(this).find('div.jsActions');
      var htmlContent = Mustache.render(templates.replyAction,
          { activityId: activityId, replyImageURL: replyImageURL,
            replyMessage: prefs.getMsg('command.reply') });
      actions.append(htmlContent);
    });
  }

  function addMoreActivitiesBarHtml() {
    var htmlContent = Mustache.render(templates.moreActivitiesBar,
        { moreActivitiesMessage: prefs.getMsg('label.show.more.activities') });
    $('#container').append(htmlContent);
  }

  function addNoMoreActivitiesTextHtml() {
    var htmlContent = Mustache.render(templates.noMoreActivitiesBar,
        { noMoreActivitiesMessage: prefs.getMsg('label.no.more.activities') });
    $('#container').append(htmlContent);
  }

  function addNewActivitiesBarHtml() {
    if ($('.jsNewActivitiesBar').length > 0) {
      return;
    }

    var htmlContent = Mustache.render(templates.newActivitiesBar,
        { newActivitiesMessage: prefs.getMsg('label.show.new.activities') });
    $('#container').prepend(htmlContent);
  }
  /* end HTML building functions */

  /* handler functions */
  function registerTabLineHandler() {
    $('a[data-filter]').click(function() {
      if (!$(this).is('.selected')) {
        $('[data-filter]').removeClass('selected');
        $(this).addClass('selected');

        filter = $(this).attr('data-filter');
        displayWallActivities();
      }
    });
  }
  
  function registerLikeStatusHandler() {
    // activities
    $('div.jsMainActivity[data-activityid]').each(function() {
      var activityId = $(this).attr('data-activityid');
      var likeIcon = $(this).find('.jsActions .jsLikeIcon');
      registerLikeStatusHandlerFor(activityId, likeIcon);
    });
    // replies
    $('div[data-replyid]').each(function() {
      var replyId = $(this).attr('data-replyid');
      var likeIcon = $(this).find('.jsReplyActions .jsLikeIcon');
      registerLikeStatusHandlerFor(replyId, likeIcon);
    });
  }

  function registerLikeStatusHandlerFor(activityId, likeIcon) {
    likeIcon.click(function() {
      var userLikeStatus = likeIcon.parents('div[data-userlikestatus]')
        .attr('data-userlikestatus');
      var operationId = userLikeStatus == 1
        ? constants.cancelLikeOperationId
        : constants.likeOperationId;

      var NXRequestParams= { operationId : operationId,
        operationParams: {
          activityId: activityId
        },
        operationContext: {},
        operationCallback: function(response, params) {
          var likeStatus = response.data;
          var i, activity;

          if ($('div.jsMainActivity[data-activityid="' + activityId + '"]').length > 0) {
            for (i = 0; i < currentActivities.length; i++) {
              activity = currentActivities[i];
              if (activity.id == activityId) {
                activity.likeStatus = likeStatus;
              }
            }

            $('div.jsMainActivity[data-activityid="' + activityId + '"]').each(function() {
              $(this).attr('data-likescount', likeStatus.likesCount);
              $(this).attr('data-userlikestatus', likeStatus.userLikeStatus);
              var actions = $(this).find('div.jsActions');
              addActivityLikeStatusHtml(actions, activityId,
                likeStatus.likesCount, likeStatus.userLikeStatus);
              registerLikeStatusHandlerFor(activityId, actions.find('.jsLikeIcon'));
            });
          } else {
            // reply
            var parentActivityId = $('div[data-replyid="' + activityId + '"]').parents('div[data-activityid]').attr('data-activityid');
            for (i = 0; i < currentActivities.length; i++) {
              activity = currentActivities[i];
              if (activity.id == parentActivityId) {
                for (var j = 0; j < activity.replies.length; j++) {
                  var reply = activity.replies[j];
                  if (reply.id == activityId) {
                    reply.likeStatus = likeStatus;
                  }
                }
              }
            }

            $('div[data-replyid="' + activityId + '"]').each(function() {
              $(this).attr('data-likescount', likeStatus.likesCount);
              $(this).attr('data-userlikestatus', likeStatus.userLikeStatus);
              var actions = $(this).find('div.jsReplyActions');
              addActivityLikeStatusHtml(actions, activityId,
                likeStatus.likesCount, likeStatus.userLikeStatus);
              registerLikeStatusHandlerFor(activityId, actions.find('.jsLikeIcon'));
            });
          }
        }
      };
      doAutomationRequest(NXRequestParams);
    });
  }

  function registerDeleteLinksHandler() {
    $('div.jsDelete[data-activityid]').click(function() {
      if (!confirmDeleteMessage()) {
        return false;
      }

      var activityId = $(this).attr("data-activityid");
      removeMiniMessage(activityId);
    });

    $('div.jsDelete[data-replyid]').each(function() {
      handleDeleteActivityReply($(this));
    });
  }

  function handleDeleteActivityReply(deleteLink) {
    deleteLink.click(function() {
      if (!confirmDeleteReply()) {
        return false;
      }

      var replyId = $(deleteLink).attr('data-replyid');
      var activityId = $(deleteLink).parents('div[data-activityid]').attr('data-activityid');
      removeActivityReply(activityId, replyId);
    });
  }

  function registerReplyLinksHandler() {
    $('.jsReply').click(function() {
      var activityId = $(this).attr('data-activityid');
      var newActivityReply = $('.jsNewActivityReply[data-activityid="' + activityId + '"]');
      updateActivityReplyMessageCounter(newActivityReply);
      newActivityReply.show();
      newActivityReply.find('textarea.jsActivityReplyText').focus();
      gadgets.window.adjustHeight();
    });
  }

  function registerNewActivityReplyHandler() {
    $('.jsActivityReplyText').keyup(function () {
      var writeButton = $(this).siblings('.newMiniMessageActions').find('.jsWriteActivityReplyButton');
      if ($(this).val().length == 0) {
        writeButton.addClass('disabled');
      } else {
        writeButton.removeClass('disabled');
      }

      var newActivityReply = $(this).parents('.jsNewActivityReply');
      updateActivityReplyMessageCounter(newActivityReply);
    });

    $('.jsNewActivityReply').each(function() {
      var newActivityReply = $(this);
      var activityId = $(this).attr('data-activityid');
      var writeButton = $(this).find('.jsWriteActivityReplyButton');
      writeButton.attr('data-activityid', activityId);
      writeButton.click(function() {
        if (newActivityReply.find('textarea.jsActivityReplyText').val().length > 0) {
          createActivityReply(newActivityReply);
        }
      });
    });
  }

  function registerMoreRepliesHandler() {
    $('.jsMoreRepliesBar').click(function() {
      var repliesContainer = $(this).parents('.jsRepliesContainer');
      repliesContainer.find('div[data-replyid]').each(function() {
        $(this).show();
      });
      repliesContainer.find('.jsMoreRepliesBar').each(function() {
        $(this).remove();
      });
      gadgets.window.adjustHeight();
    });
  }

  function registerMoreActivityBarHandler() {
    $('.jsMoreActivitiesBar').click(function() {
      showMoreActivities();
    });
  }

  function registerNewActivitiesBarHandler() {
    $('.jsNewActivitiesBar').click(function() {
      showNewActivities();
    });
  }
  /* end handler functions */

  /* mini message */
  function updateMiniMessageCounter() {
    var delta = 140 - $('.jsNewMiniMessage textarea.jsMiniMessageText').val().length;
    var miniMessageCounter = $('.jsNewMiniMessage .miniMessageCounter');
    miniMessageCounter.text(delta);
    miniMessageCounter.toggleClass('warning', delta < 5);
    if (delta < 0) {
      $('.jsWriteMiniMessageButton').attr('disabled', 'disabled');
    } else {
      $('.jsWriteMiniMessageButton').removeAttr('disabled');
    }
  }

  function confirmDeleteMessage() {
    return confirm(prefs.getMsg('label.wall.message.confirmDelete'));
  }

  function removeMiniMessage(miniMessageId) {
    var opCallParameters = {
      operationId: 'Services.RemoveMiniMessage',
      operationParams: {
        miniMessageId: miniMessageId
      },
      entityType: 'blob',
      operationContext: {},
      operationCallback: function (response, opCallParameters) {
        loadWallActivityStream();
      }
    };
    doAutomationRequest(opCallParameters);
  }
  /* end mini message */

  /* activity replies */
  function updateActivityReplyMessageCounter(newActivityReply) {
    var delta = 140 - newActivityReply.find('textarea.jsActivityReplyText').val().length;
    var miniMessageCounter = newActivityReply.find('.jsActivityReplyCounter');
    miniMessageCounter.text(delta);
    miniMessageCounter.toggleClass('warning', delta < 5);
    if (delta < 0) {
      newActivityReply.find('.jsWriteActivityReplyButton').attr('disabled', 'disabled');
    } else {
      newActivityReply.find('.jsWriteActivityReplyButton').removeAttr('disabled');
    }
  }

  function confirmDeleteReply() {
    return confirm(prefs.getMsg('label.wall.reply.confirmDelete'));
  }

  function removeActivityReply(activityId, replyId) {
    var opCallParameters = {
      operationId: 'Services.RemoveActivityReply',
      operationParams: {
        activityId: activityId,
        replyId: replyId
      },
      entityType: 'blob',
      operationContext: {},
      operationCallback: function (response, opCallParameters) {
        if (response.rc > 200 && response.rc < 300) {
          $('div[data-replyid="'+ replyId + '"]').remove();
          for (var i = 0; i < currentActivities.length; i++) {
            var activity = currentActivities[i];
            if (activity.id == activityId) {
              for (var j = 0; j < activity.replies.length; j++) {
                var reply = activity.replies[j];
                if (reply.id == replyId) {
                  activity.replies.splice(j, 1);
                }
              }
            }
          }
          gadgets.window.adjustHeight();
        }
      }
    };
    doAutomationRequest(opCallParameters);
  }
  /* end activity replies */

  function showMoreActivities() {
    var newOperationParams = jQuery.extend(true, {}, wallOperationParams);
    newOperationParams.offset = offset;
    var NXRequestParams= { operationId : constants.wallOperationId,
      operationParams: newOperationParams,
      operationContext: {},
      operationCallback: function(response, params) {
        var newActivities = response.data.activities;
        if (newActivities.length > 0) {
          currentActivities = currentActivities.concat(newActivities);
          offset = response.data.offset;
        } else {
          hasMoreActivities = false;
        }
        displayWallActivities();
      }
    };

    doAutomationRequest(NXRequestParams);
  }

  function showNewActivities() {
    currentActivities = waitingActivities;
    offset = waitingOffset;
    displayWallActivities();
  }

  // gadget initialization
  gadgets.util.registerOnLoadHandler(function() {
    var contentStyleClass = prefs.getString("contentStyleClass");
    if (contentStyleClass) {
      _gel('content').className = contentStyleClass;
    }

    registerTabLineHandler();
    
    loadWallActivityStream();
    window.setInterval(pollWallActivityStream, 30*1000);
  });

}());
