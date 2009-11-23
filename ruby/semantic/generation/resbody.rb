class ResbodyCode
  def init
    @resultVar=tmpVar
  end

  def code
    return s("rescue") if @types.size==0
    s("rescue #{exceptionType}=>#{exceptionVar}")+
    (
    if @body
      @body.code+s("#{@resultVar}=#{@body.value}")
    else
      []
    end
    ).indent
  end

  def after
    if @types.size==0
      []
    else
      s("clear #{@resultVar}")
    end
  end

  def value
    if @body
      @resultVar
    else
      "nil"
    end
  end

  private

  def exceptionVar
    @types[1]
  end

  def exceptionType
    @types[0]
  end
end