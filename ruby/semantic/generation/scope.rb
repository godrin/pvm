class ScopeCode
  def init
  end
  def code
    if @body
      @body.code
    else
      []
    end
  end
  def body
    @body
  end
  def value
    @body.value
  end
end